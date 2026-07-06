package com.lamba.app.data.trips

data class CollectedTripPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float?,
    val speedMetersPerSecond: Float?,
    val recordedAt: String
)

object TripPointSync {
    fun toBatchRequest(points: List<CollectedTripPoint>): TripPointBatchRequest {
        return TripPointBatchRequest(
            points = points.map { point ->
                TripPointCreateRequest(
                    latitude = point.latitude,
                    longitude = point.longitude,
                    accuracyM = point.accuracyMeters?.toDouble(),
                    speedKmh = point.speedMetersPerSecond?.toDouble()
                        ?.times(METERS_PER_SECOND_TO_KMH),
                    recordedAt = point.recordedAt
                )
            }
        )
    }

    private const val METERS_PER_SECOND_TO_KMH = 3.6
}

