package com.lamba.app.data.assistant

import com.google.gson.annotations.SerializedName

data class AssistantMessageRequest(
    @SerializedName("car_id")
    val carId: Int,
    @SerializedName("chat_id")
    val chatId: Int? = null,
    val message: String
)

data class AssistantMessageResponse(
    @SerializedName("assistant_message")
    val assistantMessage: String,
    val action: String,
    @SerializedName("chat_id")
    val chatId: Int? = null,
    @SerializedName("record_id")
    val recordId: Int? = null,
    @SerializedName("extracted_record")
    val extractedRecord: AssistantMessageRecord? = null,
    @SerializedName("mileage_update")
    val mileageUpdate: AssistantMileageUpdate? = null
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

data class AssistantMileageUpdate(
    @SerializedName("current_mileage_km")
    val currentMileageKm: Int
)
