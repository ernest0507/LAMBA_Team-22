package com.lamba.app.data.cars

import com.google.gson.annotations.SerializedName

data class CarDraft(
    val model: String,
    val year: Int,
    val currentMileageKm: Int,
    val notes: String? = null,
    val color: String? = null,
    val bodyType: String? = null
) {
    fun toCreateRequest(): CarCreateRequest {
        return CarCreateRequest(
            model = model,
            year = year,
            currentMileageKm = currentMileageKm,
            notes = notes,
            color = color,
            bodyType = bodyType
        )
    }
}

data class CarCreateRequest(
    val make: String? = null,
    val model: String,
    val year: Int,
    @SerializedName("current_mileage_km")
    val currentMileageKm: Int,
    val color: String? = null,
    @SerializedName("body_type")
    val bodyType: String? = null,
    val notes: String? = null
)

data class CarResponse(
    val id: Int,
    @SerializedName("owner_id")
    val ownerId: Int,
    val make: String?,
    val model: String,
    val year: Int,
    @SerializedName("current_mileage_km")
    val currentMileageKm: Int,
    val color: String?,
    @SerializedName("body_type")
    val bodyType: String?,
    val notes: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
