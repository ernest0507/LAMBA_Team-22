package com.lamba.app.data.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val accessToken: String? = null,
    val currentUser: UserResponse? = null
) {
    val isAuthenticated: Boolean = accessToken != null && currentUser != null
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authenticate {
                repository.login(email = email, password = password)
            }
        }
    }

    fun register(name: String, email: String, password: String) {
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
        _uiState.value = AuthUiState()
    }

    private suspend fun authenticate(loginCall: suspend () -> TokenResponse) {
        _uiState.update {
            it.copy(isLoading = true, errorMessage = null)
        }

        runCatching {
            val token = loginCall()
            val user = repository.me(token.accessToken)
            token to user
        }.onSuccess { (token, user) ->
            _uiState.value = AuthUiState(
                accessToken = token.accessToken,
                currentUser = user
            )
        }.onFailure { error ->
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
