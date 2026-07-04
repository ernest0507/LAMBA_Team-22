package com.lamba.app.data.records

import android.content.ContentResolver
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
    val createdRecord: MaintenanceRecordResponse? = null,
    val recordPhotos: Map<Int, RecordPhotosUiState> = emptyMap()
)

data class RecordPhotoImage(
    val id: Int,
    val filename: String,
    val contentType: String,
    val bytes: ByteArray
)

data class RecordPhotosUiState(
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false,
    val errorMessage: String? = null,
    val photos: List<RecordPhotoImage> = emptyList()
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
                    it.copy(
                        isSaving = false,
                        timeline = it.timeline.withRecord(record),
                        createdRecord = record,
                        errorMessage = null
                    )
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
        request: MaintenanceRecordCreateRequest,
        imageUris: List<String> = emptyList(),
        contentResolver: ContentResolver? = null
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
                repository.createRecord(
                    accessToken = accessToken,
                    carId = carId,
                    request = request,
                    imageUris = imageUris,
                    contentResolver = contentResolver
                )
            }.onSuccess { record ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        timeline = it.timeline.withRecord(record),
                        createdRecord = record,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isSaving = false, errorMessage = error.toRecordsMessage())
                }
            }
        }
    }

    fun loadRecordPhotos(accessToken: String?, carId: Int?, recordId: Int) {
        if (accessToken.isNullOrBlank() || carId == null) {
            return
        }

        val currentState = _uiState.value.recordPhotos[recordId]
        if (currentState?.isLoading == true || currentState?.isLoaded == true) {
            return
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    recordPhotos = state.recordPhotos + (
                        recordId to RecordPhotosUiState(isLoading = true)
                    )
                )
            }

            runCatching {
                repository.recordPhotos(accessToken, carId, recordId).map { photo ->
                    RecordPhotoImage(
                        id = photo.id,
                        filename = photo.filename,
                        contentType = photo.contentType,
                        bytes = repository.downloadRecordPhoto(
                            accessToken = accessToken,
                            carId = carId,
                            recordId = recordId,
                            photoId = photo.id
                        )
                    )
                }
            }.onSuccess { photos ->
                _uiState.update { state ->
                    state.copy(
                        recordPhotos = state.recordPhotos + (
                            recordId to RecordPhotosUiState(
                                isLoaded = true,
                                photos = photos
                            )
                        )
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        recordPhotos = state.recordPhotos + (
                            recordId to RecordPhotosUiState(
                                isLoaded = true,
                                errorMessage = error.toRecordsMessage()
                            )
                        )
                    )
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

private fun List<TimelineItemResponse>.withRecord(
    record: MaintenanceRecordResponse
): List<TimelineItemResponse> {
    return (listOf(record.toTimelineItem()) + filterNot { it.id == record.id })
        .sortedWith(
            compareByDescending<TimelineItemResponse> { it.occurredAt.orEmpty() }
                .thenByDescending { it.id }
        )
}

private fun MaintenanceRecordResponse.toTimelineItem(): TimelineItemResponse {
    return TimelineItemResponse(
        id = id,
        category = category,
        title = title,
        occurredAt = occurredAt,
        mileageKm = mileageKm,
        costAmount = costAmount
    )
}
