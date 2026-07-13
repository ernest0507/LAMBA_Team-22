package com.lamba.app.data.trips

import java.math.BigDecimal
import java.math.RoundingMode

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
                    latitude = point.latitude.roundToScale(COORDINATE_SCALE),
                    longitude = point.longitude.roundToScale(COORDINATE_SCALE),
                    accuracyM = point.accuracyMeters?.toDouble()?.roundToScale(METRIC_SCALE),
                    speedKmh = point.speedMetersPerSecond?.toDouble()
                        ?.times(METERS_PER_SECOND_TO_KMH)
                        ?.roundToScale(METRIC_SCALE),
                    recordedAt = point.recordedAt
                )
            }
        )
    }

    private fun Double.roundToScale(scale: Int): Double {
        return BigDecimal.valueOf(this)
            .setScale(scale, RoundingMode.HALF_UP)
            .toDouble()
    }

    private const val METERS_PER_SECOND_TO_KMH = 3.6
    private const val COORDINATE_SCALE = 7
    private const val METRIC_SCALE = 2
}

