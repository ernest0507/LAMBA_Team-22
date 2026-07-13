package com.lamba.app.data.trips.tracking

data class TripTrackingPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float?,
    val speedMetersPerSecond: Float?,
    val recordedAtMillis: Long
)

data class TripTrackingUpdate(
    val snapshot: TripTrackingSnapshot,
    val acceptedPoint: TripTrackingPoint?
)

data class TripTrackingSnapshot(
    val isTracking: Boolean,
    val startedAtMillis: Long?,
    val elapsedSeconds: Long,
    val distanceMeters: Double,
    val currentSpeedMetersPerSecond: Float,
    val averageSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val latestPoint: TripTrackingPoint?
) {
    val currentSpeedKmh: Double
        get() = currentSpeedMetersPerSecond * METERS_PER_SECOND_TO_KMH

    companion object {
        private const val METERS_PER_SECOND_TO_KMH = 3.6

        val Idle = TripTrackingSnapshot(
            isTracking = false,
            startedAtMillis = null,
            elapsedSeconds = 0L,
            distanceMeters = 0.0,
            currentSpeedMetersPerSecond = 0f,
            averageSpeedKmh = 0.0,
            maxSpeedKmh = 0.0,
            latestPoint = null
        )
    }
}
