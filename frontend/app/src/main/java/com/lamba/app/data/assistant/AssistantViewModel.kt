package com.lamba.app.data.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamba.app.screens.home.ChatMessage
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

data class AssistantUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false,
    val errorMessage: String? = null,
    val activeChatId: Int? = null,
    val lastResponse: AssistantMessageResponse? = null
)

class AssistantViewModel(
    private val repository: AssistantRepository = AssistantRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(AssistantUiState())
    val uiState: StateFlow<AssistantUiState> = _uiState.asStateFlow()

    fun sendMessage(accessToken: String?, carId: Int?, message: String) {
        val cleanMessage = message.trim()
        if (cleanMessage.isEmpty()) return

        if (accessToken.isNullOrBlank()) {
            addAssistantMessage("Sign in before using LAMBA AI.")
            return
        }

        if (carId == null) {
            addAssistantMessage("Create a digital twin before using LAMBA AI.")
            return
        }

        val activeChatId = _uiState.value.activeChatId
        _uiState.update {
            it.copy(
                messages = it.messages + ChatMessage(cleanMessage, isUser = true),
                isSending = true,
                errorMessage = null,
                lastResponse = null
            )
        }

        viewModelScope.launch {
            runCatching {
                repository.sendMessage(
                    accessToken = accessToken,
                    carId = carId,
                    chatId = activeChatId,
                    message = cleanMessage
                )
            }.onSuccess { response ->
                _uiState.update {
                    it.copy(
                        messages = it.messages + ChatMessage(
                            text = response.assistantMessage,
                            isUser = false
                        ),
                        isSending = false,
                        errorMessage = null,
                        activeChatId = response.chatId ?: activeChatId,
                        lastResponse = response
                    )
                }
            }.onFailure { error ->
                val messageText = error.toAssistantMessage()
                _uiState.update {
                    it.copy(
                        messages = it.messages + ChatMessage(messageText, isUser = false),
                        isSending = false,
                        errorMessage = messageText
                    )
                }
            }
        }
    }

    fun consumeLastResponse() {
        _uiState.update {
            it.copy(lastResponse = null)
        }
    }

    private fun addAssistantMessage(message: String) {
        _uiState.update {
            it.copy(
                messages = it.messages + ChatMessage(message, isUser = false),
                isSending = false,
                errorMessage = message
            )
        }
    }

    private fun Throwable.toAssistantMessage(): String {
        return when (this) {
            is HttpException -> when (code()) {
                401 -> "Session expired. Please sign in again."
                404 -> {
                    val detail = errorDetail()
                    if (detail == "Car not found") {
                        "Digital twin was not found."
                    } else {
                        "LAMBA AI endpoint was not found on backend."
                    }
                }
                422 -> "LAMBA AI could not process this message."
                else -> "LAMBA AI request failed. HTTP ${code()}."
            }
            is IOException -> "Cannot reach backend. Check the connection."
            else -> message ?: "Assistant request failed."
        }
    }

    private fun HttpException.errorDetail(): String? {
        return runCatching {
            response()?.errorBody()?.string()?.let { body ->
                JSONObject(body).optString("detail").takeIf { it.isNotBlank() }
            }
        }.getOrNull()
    }
}
