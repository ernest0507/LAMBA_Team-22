package com.lamba.app.data.assistant

import com.google.gson.annotations.SerializedName

data class AssistantMessageRequest(
    @SerializedName("car_id")
    val carId: Int,
    val message: String
)

data class AssistantMessageResponse(
    @SerializedName("assistant_message")
    val assistantMessage: String,
    val action: String,
    @SerializedName("record_id")
    val recordId: Int?,
    @SerializedName("extracted_record")
    val extractedRecord: AssistantMessageRecord?
)

data class AssistantMessageRecord(
    val category: String?,
    val title: String?,
    val description: String?,
    @SerializedName("occurred_at")
    val occurredAt: String?,
    @SerializedName("mileage_km")
    val mileageKm: Int?,
    @SerializedName("cost_amount")
    val costAmount: String?,
    val vendor: String?
)
