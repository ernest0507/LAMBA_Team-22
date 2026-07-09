package com.lamba.app.data.trips

import com.google.gson.annotations.SerializedName

data class TripStartRequest(
    @SerializedName("started_at")
    val startedAt: String? = null
)

data class TripFinishRequest(
    @SerializedName("ended_at")
    val endedAt: String? = null
)

data class TripPointCreateRequest(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("accuracy_m")
    val accuracyM: Double? = null,
    @SerializedName("speed_kmh")
    val speedKmh: Double? = null,
    @SerializedName("recorded_at")
    val recordedAt: String
)

data class TripPointBatchRequest(
    val points: List<TripPointCreateRequest>
)

data class TripResponse(
    val id: Int,
    @SerializedName("car_id")
    val carId: Int,
    val status: String = "",
    @SerializedName("started_at")
    val startedAt: String,
    @SerializedName("ended_at")
    val endedAt: String?,
    @SerializedName("distance_m")
    val distanceM: String,
    @SerializedName("duration_seconds")
    val durationSeconds: Int,
    @SerializedName("average_speed_kmh")
    val averageSpeedKmh: String,
    @SerializedName("max_speed_kmh")
    val maxSpeedKmh: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val points: List<TripPointResponse> = emptyList()
)

data class TripPointResponse(
    val id: Int,
    @SerializedName("trip_id")
    val tripId: Int,
    val latitude: String,
    val longitude: String,
    @SerializedName("accuracy_m")
    val accuracyM: String?,
    @SerializedName("speed_kmh")
    val speedKmh: String?,
    @SerializedName("recorded_at")
    val recordedAt: String,
    @SerializedName("created_at")
    val createdAt: String
)

