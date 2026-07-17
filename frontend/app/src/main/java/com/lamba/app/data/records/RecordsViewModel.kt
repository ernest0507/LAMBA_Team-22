package com.lamba.app.data.records

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
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
    val recordPhotos: Map<Int, RecordPhotosUiState> = emptyMap(),
    val isScanningReceipt: Boolean = false,
    val scannedReceipt: ReceiptResponse? = null,
    val receiptScanSuccessId: Int? = null,
    val isDuplicateReceipt: Boolean = false
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

    fun consumeScannedReceipt() {
        _uiState.update {
            it.copy(scannedReceipt = null, receiptScanSuccessId = null)
        }
    }

    fun consumeDuplicateReceipt() {
        _uiState.update {
            it.copy(isDuplicateReceipt = false)
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
                404 -> "Receipt scan endpoint or current car was not found."
                422 -> "Check the expense data and try again."
                else -> "Backend error: HTTP ${code()}."
            }
            is IOException -> "Cannot reach backend. Start the backend and check the base URL."
            else -> message ?: "Records request failed."
        }
    }

    fun scanReceipt(accessToken: String?, carId: Int?, qrRaw: String) {
        if (accessToken.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Sign in before scanning receipts.")
            }
            return
        }

        if (carId == null) {
            _uiState.update {
                it.copy(errorMessage = "Create a digital twin before scanning receipts.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isScanningReceipt = true,
                    isSaving = true,
                    errorMessage = null,
                    scannedReceipt = null,
                    receiptScanSuccessId = null,
                    isDuplicateReceipt = false,
                    createdRecord = null
                )
            }

            runCatching {
                var scannedReceipt: ReceiptResponse? = null
                val request = try {
                    val receipt = repository.scanReceipt(
                        accessToken = accessToken,
                        carId = carId,
                        qrRaw = qrRaw
                    )
                    scannedReceipt = receipt
                    receipt.toRecordRequest(qrRaw)
                } catch (error: Throwable) {
                    if (error.canFallbackToQrRecord()) {
                        qrRaw.toFallbackReceiptRecordRequest()
                    } else {
                        throw error
                    }
                }
                val record = repository.createRecord(
                    accessToken = accessToken,
                    carId = carId,
                    request = request
                )
                scannedReceipt to record
            }.onSuccess { (receipt, record) ->
                _uiState.update {
                    it.copy(
                        isScanningReceipt = false,
                        isSaving = false,
                        scannedReceipt = receipt,
                        receiptScanSuccessId = record.id,
                        timeline = it.timeline.withRecord(record),
                        createdRecord = null,
                        isDuplicateReceipt = false,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isScanningReceipt = false,
                        isSaving = false,
                        isDuplicateReceipt = error.isDuplicateReceiptError(),
                        errorMessage = if (error.isDuplicateReceiptError()) {
                            null
                        } else {
                            error.toRecordsMessage()
                        }
                    )
                }
            }
        }
    }
}

private fun Throwable.canFallbackToQrRecord(): Boolean {
    return this !is HttpException || code() !in setOf(401, 404, 409)
}

private fun Throwable.isDuplicateReceiptError(): Boolean {
    return this is HttpException && code() == 409
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
        description = description,
        occurredAt = occurredAt,
        mileageKm = mileageKm,
        costAmount = costAmount,
        vendor = vendor,
        receipt = receipt
    )
}

private fun String.toFallbackReceiptRecordRequest(): MaintenanceRecordCreateRequest {
    val params = parseQrParams()
    val amount = params["s"].toReceiptAmount()
    val occurredAt = params["t"]?.toQrDate()
    val receiptTime = params["t"]?.toQrTime()
    val description = listOfNotNull(
        "Receipt category: траты",
        amount.takeIf { it != "0.00" }?.let { "Receipt amount: $it" },
        receiptTime?.let { "Receipt time: $it" },
        params["fn"]?.let { "Fiscal drive number: $it" },
        params["i"]?.let { "Fiscal document number: $it" },
        params["fp"]?.let { "Fiscal sign: $it" },
        "QR: $this"
    ).joinToString(separator = "\n")

    return MaintenanceRecordCreateRequest(
        category = "заправка",
        title = "Заправка",
        description = description,
        occurredAt = occurredAt,
        costAmount = amount
    )
}

private fun ReceiptResponse.toRecordRequest(qrRaw: String): MaintenanceRecordCreateRequest {
    val gasStation = sellerName?.trim().orEmpty()
    val receiptDate = ticketDate?.take(10)
    val fuelType = primaryFuelItemName()
    val fuelLiters = primaryFuelQuantity()
    val pumpNumber = pumpNumberFromReceipt()
    val description = listOfNotNull(
        pumpNumber?.let { "Pump number: $it" },
        fuelType?.let { "Fuel mark: $it" },
        fuelLiters?.let { "Liters: $it" },
        gasStation.takeIf { it.isNotBlank() }?.let { "Gas station: $it" },
        retailPlaceAddress?.trim()?.takeIf { it.isNotBlank() }?.let { "Address: $it" }
    ).joinToString(separator = "\n")

    return MaintenanceRecordCreateRequest(
        category = "заправка",
        title = "Заправка",
        description = description,
        occurredAt = receiptDate,
        costAmount = totalAmount?.takeIf { it.isNotBlank() } ?: "0.00",
        vendor = gasStation.takeIf { it.isNotBlank() },
        receipt = toRecordReceiptPayload()
    )
}

private fun String.parseQrParams(): Map<String, String> {
    return split("&")
        .mapNotNull { part ->
            val separatorIndex = part.indexOf("=")
            if (separatorIndex <= 0) {
                null
            } else {
                val key = part.substring(0, separatorIndex).trim().lowercase()
                val value = part.substring(separatorIndex + 1).urlDecode().trim()
                key to value
            }
        }
        .toMap()
}

private fun String.urlDecode(): String {
    return runCatching {
        URLDecoder.decode(this, StandardCharsets.UTF_8.name())
    }.getOrDefault(this)
}

private fun String?.toReceiptAmount(): String {
    val normalized = this
        ?.trim()
        ?.replace(",", ".")
        ?.filter { it.isDigit() || it == '.' }
        ?.takeIf { it.isNotBlank() }
        ?: return "0.00"

    return runCatching {
        BigDecimal(normalized).setScale(2, RoundingMode.HALF_UP).toPlainString()
    }.getOrDefault("0.00")
}

private fun String.toQrDate(): String? {
    val compactDate = take(8)
    if (compactDate.length != 8 || compactDate.any { !it.isDigit() }) {
        return null
    }
    return "${compactDate.substring(0, 4)}-${compactDate.substring(4, 6)}-${compactDate.substring(6, 8)}"
}

private fun String.toQrTime(): String? {
    val markerIndex = indexOf('T')
    if (markerIndex < 0 || length < markerIndex + 5) {
        return null
    }
    val hour = substring(markerIndex + 1, markerIndex + 3)
    val minute = substring(markerIndex + 3, markerIndex + 5)
    if ((hour + minute).any { !it.isDigit() }) {
        return null
    }
    return "$hour:$minute"
}

private fun ReceiptResponse.toRecordReceiptPayload(): RecordReceiptPayload {
    return RecordReceiptPayload(
        receiptId = receiptId,
        sellerName = sellerName,
        sellerInn = sellerInn,
        retailPlaceAddress = retailPlaceAddress,
        ticketDate = ticketDate,
        totalAmount = totalAmount,
        fiscalDriveNumber = fiscalDriveNumber,
        fiscalDocumentNumber = fiscalDocumentNumber,
        fiscalSign = fiscalSign,
        items = items
    )
}

private fun ReceiptResponse.primaryFuelItemName(): String? {
    return items.firstNotNullOfOrNull { item ->
        item.name
            ?.trim()
            ?.takeIf { name -> name.isNotBlank() }
            ?.takeIf { name -> name.isLikelyFuelName() }
            ?.extractFuelType()
    } ?: items.firstOrNull()?.name?.trim()?.takeIf { it.isNotBlank() }
}

private fun ReceiptResponse.primaryFuelQuantity(): String? {
    val quantity = items.firstNotNullOfOrNull { item ->
        item.quantity
            ?.trim()
            ?.takeIf { value -> value.isNotBlank() }
            ?.takeIf { item.name?.isLikelyFuelName() == true }
    }

    return quantity.trimReceiptNumber()
}

private fun String?.trimReceiptNumber(): String? {
    return this
        ?.trim()
        ?.replace(',', '.')
        ?.trimEnd('0')
        ?.trimEnd('.')
        ?.takeIf { it.isNotBlank() }
}

private fun String.extractFuelType(): String {
    val patterns = listOf(
        Regex("""(?i)\b(?:АИ|AI)\s*[- ]?\s*\d{2,3}(?:[- ]?[A-ZА-Я0-9]+)?\b"""),
        Regex("""(?i)\b(?:ДТ|ДИЗЕЛЬ|DIESEL|GAS|FUEL)\b""")
    )

    return patterns.firstNotNullOfOrNull { pattern ->
        pattern.find(this)?.value
    }?.replace(Regex("""\s+"""), " ")
        ?.trim()
        ?: this
}

private fun ReceiptResponse.pumpNumberFromReceipt(): String? {
    val candidates = items.mapNotNull { it.name?.trim() }
    val patterns = listOf(
        Regex("""(?i)(?:колонк[а-я]*|pump|column)\s*(?:№|#|N)?\s*[:\-]?\s*(\d+)"""),
        Regex("""(?i)(?:трк|trk)\s*(?:№|#|N)?\s*[:\-]?\s*(\d+)""")
    )

    return candidates.firstNotNullOfOrNull { candidate ->
        patterns.firstNotNullOfOrNull { pattern ->
            pattern.find(candidate)?.groupValues?.getOrNull(1)
        }
    } ?: requestNumber?.toString()
}

private fun String.isLikelyFuelName(): Boolean {
    val normalized = uppercase()
    return listOf("АИ", "AI", "БЕНЗ", "ДТ", "ДИЗ", "GAS", "FUEL").any { marker ->
        normalized.contains(marker)
    }
}

private fun String.toReceiptTimeOrNull(): String? {
    val timePart = substringAfter('T', missingDelimiterValue = "")
        .ifBlank { substringAfter(' ', missingDelimiterValue = "") }
    if (timePart.length < 5) return null
    return timePart.take(5)
}

private fun ReceiptResponse.receiptItemsDescription(): String {
    if (items.isEmpty()) return ""

    return items.joinToString(
        separator = "\n",
        prefix = "Items:\n"
    ) { item ->
        val parts = listOfNotNull(
            item.name?.takeIf { it.isNotBlank() },
            item.quantity?.takeIf { it.isNotBlank() }?.let { "qty: $it" },
            item.priceAmount?.takeIf { it.isNotBlank() }?.let { "price: $it" },
            item.totalAmount?.takeIf { it.isNotBlank() }?.let { "sum: $it" }
        )
        "- ${parts.joinToString(separator = ", ")}"
    }
}
