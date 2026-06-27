package com.lamba.app.data.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class RecordsUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val timeline: List<TimelineItemResponse> = emptyList(),
    val createdRecord: MaintenanceRecordResponse? = null
)

class RecordsViewModel(
    private val repository: RecordsRepository = RecordsRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState: StateFlow<RecordsUiState> = _uiState.asStateFlow()

    fun loadTimeline(accessToken: String?, carId: Int?) {
        if (accessToken.isNullOrBlank() || carId == null) {
            _uiState.update {
                it.copy(errorMessage = "Create a digital twin before viewing history.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            runCatching {
                repository.timeline(accessToken, carId)
            }.onSuccess { items ->
                _uiState.update {
                    it.copy(isLoading = false, timeline = items, errorMessage = null)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = error.toRecordsMessage())
                }
            }
        }
    }

    fun createExpense(accessToken: String?, carId: Int?, draft: ExpenseDraft) {
        if (accessToken.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Sign in before adding expenses.")
            }
            return
        }

        if (carId == null) {
            _uiState.update {
                it.copy(errorMessage = "Create a digital twin before adding expenses.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true, errorMessage = null, createdRecord = null)
            }

            runCatching {
                repository.createExpense(accessToken, carId, draft)
            }.onSuccess { record ->
                _uiState.update {
                    it.copy(isSaving = false, createdRecord = record, errorMessage = null)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isSaving = false, errorMessage = error.toRecordsMessage())
                }
            }
        }
    }

    fun createRecord(
        accessToken: String?,
        carId: Int?,
        request: MaintenanceRecordCreateRequest
    ) {
        if (accessToken.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Sign in before adding records.")
            }
            return
        }

        if (carId == null) {
            _uiState.update {
                it.copy(errorMessage = "Create a digital twin before adding records.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true, errorMessage = null, createdRecord = null)
            }

            runCatching {
                repository.createRecord(accessToken, carId, request)
            }.onSuccess { record ->
                _uiState.update {
                    it.copy(isSaving = false, createdRecord = record, errorMessage = null)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isSaving = false, errorMessage = error.toRecordsMessage())
                }
            }
        }
    }

    fun consumeCreatedRecord() {
        _uiState.update {
            it.copy(createdRecord = null)
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun Throwable.toRecordsMessage(): String {
        return when (this) {
            is HttpException -> when (code()) {
                401 -> "Session expired. Sign in again."
                404 -> "Car was not found. Create a digital twin first."
                422 -> "Check the expense data and try again."
                else -> "Backend error: HTTP ${code()}."
            }
            is IOException -> "Cannot reach backend. Start the backend and check the base URL."
            else -> message ?: "Records request failed."
        }
    }
}
