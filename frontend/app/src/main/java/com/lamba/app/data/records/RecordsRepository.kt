package com.lamba.app.data.records

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.time.LocalDate
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class RecordsRepository(
    private val api: RecordsApi = RecordsNetwork.api
) {
    suspend fun createRecord(
        accessToken: String,
        carId: Int,
        request: MaintenanceRecordCreateRequest,
        imageUris: List<String> = emptyList(),
        contentResolver: ContentResolver? = null
    ): MaintenanceRecordResponse {
        val record = api.createRecord(
            authorization = "Bearer $accessToken",
            carId = carId,
            request = request
        )

        if (imageUris.isNotEmpty() && contentResolver != null) {
            uploadRecordPhotos(
                accessToken = accessToken,
                carId = carId,
                recordId = record.id,
                imageUris = imageUris,
                contentResolver = contentResolver
            )
        }

        return record
    }

    suspend fun createExpense(
        accessToken: String,
        carId: Int,
        draft: ExpenseDraft
    ): MaintenanceRecordResponse {
        val cleanDescription = draft.description.trim()
        val title = cleanDescription.ifBlank { "Expense" }

        return createRecord(
            accessToken = accessToken,
            carId = carId,
            request = MaintenanceRecordCreateRequest(
                title = title,
                description = cleanDescription.takeIf { it.isNotEmpty() },
                occurredAt = LocalDate.now().toString(),
                costAmount = draft.amount.toString()
            )
        )
    }

    suspend fun uploadRecordPhotos(
        accessToken: String,
        carId: Int,
        recordId: Int,
        imageUris: List<String>,
        contentResolver: ContentResolver
    ): List<RecordPhotoResponse> {
        val parts = imageUris.toPhotoParts(contentResolver)
        if (parts.isEmpty()) return emptyList()

        return api.uploadRecordPhotos(
            authorization = "Bearer $accessToken",
            carId = carId,
            recordId = recordId,
            files = parts
        )
    }

    suspend fun recordPhotos(
        accessToken: String,
        carId: Int,
        recordId: Int
    ): List<RecordPhotoResponse> {
        return api.recordPhotos(
            authorization = "Bearer $accessToken",
            carId = carId,
            recordId = recordId
        )
    }

    suspend fun downloadRecordPhoto(
        accessToken: String,
        carId: Int,
        recordId: Int,
        photoId: Int
    ): ByteArray {
        return api.downloadRecordPhoto(
            authorization = "Bearer $accessToken",
            carId = carId,
            recordId = recordId,
            photoId = photoId
        ).use { body ->
            body.bytes()
        }
    }

    suspend fun timeline(accessToken: String, carId: Int): List<TimelineItemResponse> {
        return api.timeline(
            authorization = "Bearer $accessToken",
            carId = carId
        )
    }

    suspend fun scanReceipt(
        accessToken: String,
        carId: Int,
        qrRaw: String
    ): ReceiptResponse {
        return api.scanReceipt(
            authorization = "Bearer $accessToken",
            carId = carId,
            request = ReceiptScanRequest(qrraw = qrRaw)
        )
    }

}

private fun List<String>.toPhotoParts(contentResolver: ContentResolver): List<MultipartBody.Part> {
    return take(3).mapIndexedNotNull { index, uriString ->
        val uri = Uri.parse(uriString)
        val contentType = contentResolver.getType(uri) ?: "image/jpeg"
        val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: return@mapIndexedNotNull null
        val filename = contentResolver.displayName(uri) ?: "photo-${index + 1}.jpg"
        val requestBody = bytes.toRequestBody(contentType.toMediaTypeOrNull())

        MultipartBody.Part.createFormData(
            name = "files",
            filename = filename,
            body = requestBody
        )
    }
}

private fun ContentResolver.displayName(uri: Uri): String? {
    return query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
        val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (displayNameIndex >= 0 && cursor.moveToFirst()) {
            cursor.getString(displayNameIndex)
        } else {
            null
        }
    }
}
