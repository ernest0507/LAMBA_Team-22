package com.lamba.app.data.records

import com.google.gson.annotations.SerializedName

data class ExpenseDraft(
    val amount: Int,
    val description: String
)

data class MaintenanceRecordCreateRequest(
    val category: String = "expense",
    val title: String,
    val description: String? = null,
    @SerializedName("occurred_at")
    val occurredAt: String? = null,
    @SerializedName("mileage_km")
    val mileageKm: Long? = null,
    @SerializedName("cost_amount")
    val costAmount: String,
    val vendor: String? = null,
    val receipt: RecordReceiptPayload? = null
)

data class MaintenanceRecordResponse(
    val id: Int,
    @SerializedName("car_id")
    val carId: Int,
    val category: String?,
    val title: String?,
    val description: String?,
    @SerializedName("occurred_at")
    val occurredAt: String?,
    @SerializedName("mileage_km")
    val mileageKm: Long?,
    @SerializedName("cost_amount")
    val costAmount: String,
    val vendor: String?,
    val receipt: RecordReceiptPayload? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class RecordPhotoResponse(
    val id: Int,
    @SerializedName("record_id")
    val recordId: Int,
    val filename: String,
    @SerializedName("content_type")
    val contentType: String,
    @SerializedName("size_bytes")
    val sizeBytes: Int,
    @SerializedName("created_at")
    val createdAt: String,
    val url: String
)

data class TimelineItemResponse(
    val id: Int,
    val category: String?,
    val title: String?,
    val description: String? = null,
    @SerializedName("occurred_at")
    val occurredAt: String?,
    @SerializedName("mileage_km")
    val mileageKm: Long?,
    @SerializedName("cost_amount")
    val costAmount: String,
    val vendor: String? = null,
    val receipt: RecordReceiptPayload? = null
)


data class ReceiptScanRequest(
    val qrraw: String
)


data class ReceiptResponse(
    @SerializedName("receipt_id")
    val receiptId: String,
    @SerializedName("provider_code")
    val providerCode: Int,
    val status: String,
    @SerializedName("seller_name")
    val sellerName: String?,
    @SerializedName("seller_inn")
    val sellerInn: String?,
    @SerializedName("retail_place_address")
    val retailPlaceAddress: String?,
    @SerializedName("ticket_date")
    val ticketDate: String?,
    @SerializedName("request_number")
    val requestNumber: Int?,
    @SerializedName("total_amount")
    val totalAmount: String?,
    @SerializedName("fiscal_drive_number")
    val fiscalDriveNumber: String?,
    @SerializedName("fiscal_document_number")
    val fiscalDocumentNumber: String?,
    @SerializedName("fiscal_sign")
    val fiscalSign: String?,
    val items: List<ReceiptItemResponse> = emptyList()
)

data class RecordReceiptPayload(
    @SerializedName("receipt_id")
    val receiptId: String? = null,
    @SerializedName("seller_name")
    val sellerName: String? = null,
    @SerializedName("seller_inn")
    val sellerInn: String? = null,
    @SerializedName("retail_place_address")
    val retailPlaceAddress: String? = null,
    @SerializedName("ticket_date")
    val ticketDate: String? = null,
    @SerializedName("total_amount")
    val totalAmount: String? = null,
    @SerializedName("fiscal_drive_number")
    val fiscalDriveNumber: String? = null,
    @SerializedName("fiscal_document_number")
    val fiscalDocumentNumber: String? = null,
    @SerializedName("fiscal_sign")
    val fiscalSign: String? = null,
    val items: List<ReceiptItemResponse> = emptyList()
)

data class ReceiptItemResponse(
    val id: Int? = null,
    @SerializedName("record_id")
    val recordId: Int? = null,
    val name: String?,
    @SerializedName("price_amount")
    val priceAmount: String?,
    val quantity: String?,
    @SerializedName("total_amount")
    val totalAmount: String?
)























