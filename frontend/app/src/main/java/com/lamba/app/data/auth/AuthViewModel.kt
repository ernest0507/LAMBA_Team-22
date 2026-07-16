package com.lamba.app.data.auth

import android.app.Application
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import org.json.JSONObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.HttpException

data class AuthUiState(
    val isLoading: Boolean = false,
    val isRestoringSession: Boolean = false,
    val sessionExpired: Boolean = false,
    val errorMessage: String? = null,
    val accessToken: String? = null,
    val currentUser: UserResponse? = null
) {
    val isAuthenticated: Boolean = accessToken?.isJwtStillValid() == true && currentUser != null
}

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repository = AuthRepository()
    private val tokenStore = AuthTokenStore(application)
    private val cachedAccessToken = tokenStore.getAccessToken()
    private val cachedUser = tokenStore.getUser()
    private val hasValidCachedToken = cachedAccessToken?.isJwtStillValid() == true

    private val _uiState = MutableStateFlow(
        AuthUiState(
            isRestoringSession = hasValidCachedToken && cachedUser == null,
            accessToken = cachedAccessToken.takeIf { hasValidCachedToken },
            currentUser = cachedUser.takeIf { hasValidCachedToken }
        )
    )
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        restoreCachedSession()
    }

    fun login(email: String, password: String) {
        if (_uiState.value.isLoading) return
        _uiState.update {
            it.copy(isLoading = true, errorMessage = null)
        }

        viewModelScope.launch {
            authenticate {
                repository.login(email = email, password = password)
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (_uiState.value.isLoading) return
        _uiState.update {
            it.copy(isLoading = true, errorMessage = null)
        }

        viewModelScope.launch {
            authenticate {
                repository.register(name = name, email = email, password = password)
                repository.login(email = email, password = password)
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun logout() {
        tokenStore.clearSession()
        _uiState.value = AuthUiState()
    }

    fun expireSession() {
        tokenStore.clearSession()
        _uiState.value = AuthUiState(sessionExpired = true)
    }

    private fun restoreCachedSession() {
        val cachedToken = cachedAccessToken ?: return

        if (!hasValidCachedToken) {
            tokenStore.clearSession()
            _uiState.value = AuthUiState()
            return
        }

        viewModelScope.launch {
            if (cachedUser != null) {
                validateCachedSession(cachedToken)
                return@launch
            }

            _uiState.update {
                it.copy(
                    isRestoringSession = true,
                    errorMessage = null,
                    accessToken = cachedToken
                )
            }

            val user = runCatching {
                withTimeoutOrNull(RESTORE_SESSION_TIMEOUT_MS) {
                    repository.me(cachedToken)
                }
            }.getOrNull()

            if (user != null) {
                _uiState.value = AuthUiState(
                    accessToken = cachedToken,
                    currentUser = user
                )
                tokenStore.saveSession(cachedToken, user)
            } else {
                tokenStore.clearSession()
                _uiState.value = AuthUiState(isRestoringSession = false)
            }
        }
    }

    private suspend fun validateCachedSession(cachedToken: String) {
        val user = runCatching {
            repository.me(cachedToken)
        }.getOrNull()

        if (user != null) {
            tokenStore.saveSession(cachedToken, user)
            _uiState.update {
                it.copy(currentUser = user)
            }
        } else {
            tokenStore.clearSession()
            _uiState.value = AuthUiState(sessionExpired = true)
        }
    }

    private companion object {
        const val RESTORE_SESSION_TIMEOUT_MS = 1_500L
    }

    private suspend fun authenticate(loginCall: suspend () -> TokenResponse) {
        runCatching {
            val token = loginCall()
            val user = repository.me(token.accessToken)
            token to user
        }.onSuccess { (token, user) ->
            _uiState.value = AuthUiState(
                accessToken = token.accessToken,
                currentUser = user
            )
            tokenStore.saveSession(token.accessToken, user)
        }.onFailure { error ->
            tokenStore.clearSession()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = error.toAuthMessage()
                )
            }
        }
    }

    private fun Throwable.toAuthMessage(): String {
        return when (this) {
            is HttpException -> when (code()) {
                401 -> "Invalid email or password."
                409 -> "This email is already registered."
                else -> "Backend error: HTTP ${code()}."
            }
            is IOException -> "Cannot reach backend. Start the backend and check the base URL."
            else -> message ?: "Authentication failed."
        }
    }
}

private fun String.isJwtStillValid(): Boolean {
    return runCatching {
        val payload = split(".").getOrNull(1) ?: return false
        val decodedPayload = String(
            Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP),
            Charsets.UTF_8
        )
        val expiresAtSeconds = JSONObject(decodedPayload).optLong("exp", 0L)
        val nowSeconds = System.currentTimeMillis() / 1000L

        expiresAtSeconds > nowSeconds + TOKEN_EXPIRY_GRACE_SECONDS
    }.getOrDefault(false)
}

private const val TOKEN_EXPIRY_GRACE_SECONDS = 30L
