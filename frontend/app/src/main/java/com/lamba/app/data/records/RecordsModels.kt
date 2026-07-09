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
    val mileageKm: Int? = null,
    @SerializedName("cost_amount")
    val costAmount: String,
    val vendor: String? = null
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
    val mileageKm: Int?,
    @SerializedName("cost_amount")
    val costAmount: String,
    val vendor: String?,
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
    val mileageKm: Int?,
    @SerializedName("cost_amount")
    val costAmount: String,
    val vendor: String? = null
)


data class ReceiptScanRequest(
    val qrraw: String
)


data class ReceiptResponse(
    @SerializedName("provider_code")
    val providerCode: Int,
    val status: String,
    @SerializedName("seller_name")
    val sellerName: String?,
    @SerializedName("seller_inn")
    val sellerInn: String?,
    @SerializedName("ticket_date")
    val ticketDate: String?,
    @SerializedName("total_amount")
    val totalAmount: String?,
    val items: List<ReceiptItemResponse> = emptyList()
)

data class ReceiptItemResponse(
    val name: String?,
    @SerializedName("price_amount")
    val priceAmount: String?,
    val quantity: String?,
    @SerializedName("total_amount")
    val totalAmount: String?
)























