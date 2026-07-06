package com.lamba.app.data.trips.tracking

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class TripSessionTracker(
    private val nowMillis: () -> Long = { System.currentTimeMillis() }
) {
    private val points = mutableListOf<TripTrackingPoint>()
    private var startedAtMillis: Long? = null
    private var distanceMeters: Double = 0.0
    private var maxSpeedMetersPerSecond: Float = 0f

    fun start(startedAtMillis: Long = nowMillis()): TripTrackingSnapshot {
        points.clear()
        this.startedAtMillis = startedAtMillis
        distanceMeters = 0.0
        maxSpeedMetersPerSecond = 0f
        return snapshot(startedAtMillis)
    }

    fun addPoint(point: TripTrackingPoint): TripTrackingSnapshot {
        if (startedAtMillis == null) {
            startedAtMillis = point.recordedAtMillis
        }

        val previousPoint = points.lastOrNull()
        if (previousPoint != null) {
            distanceMeters += distanceBetweenMeters(previousPoint, point)
        }

        points += point
        val currentSpeed = point.speedMetersPerSecond ?: speedBetween(previousPoint, point)
        maxSpeedMetersPerSecond = maxOf(maxSpeedMetersPerSecond, currentSpeed)

        return snapshot(point.recordedAtMillis)
    }

    fun stop(stoppedAtMillis: Long = nowMillis()): TripTrackingSnapshot {
        val stoppedSnapshot = snapshot(stoppedAtMillis).copy(isTracking = false)
        points.clear()
        startedAtMillis = null
        distanceMeters = 0.0
        maxSpeedMetersPerSecond = 0f
        return stoppedSnapshot
    }

    fun snapshot(atMillis: Long = nowMillis()): TripTrackingSnapshot {
        val start = startedAtMillis
        val elapsedSeconds = if (start == null) {
            0L
        } else {
            ((atMillis - start).coerceAtLeast(0L)) / MILLIS_PER_SECOND
        }
        val latestPoint = points.lastOrNull()
        val currentSpeed = latestPoint?.speedMetersPerSecond
            ?: speedBetween(points.getOrNull(points.lastIndex - 1), latestPoint)
        val averageSpeedKmh = if (elapsedSeconds > 0L) {
            distanceMeters / elapsedSeconds * SECONDS_PER_HOUR / METERS_PER_KILOMETER
        } else {
            0.0
        }

        return TripTrackingSnapshot(
            isTracking = start != null,
            startedAtMillis = start,
            elapsedSeconds = elapsedSeconds,
            distanceMeters = distanceMeters,
            currentSpeedMetersPerSecond = currentSpeed,
            averageSpeedKmh = averageSpeedKmh,
            maxSpeedKmh = maxSpeedMetersPerSecond * METERS_PER_SECOND_TO_KMH,
            latestPoint = latestPoint
        )
    }

    private fun speedBetween(
        previousPoint: TripTrackingPoint?,
        currentPoint: TripTrackingPoint?
    ): Float {
        if (previousPoint == null || currentPoint == null) {
            return 0f
        }

        val elapsedSeconds =
            (currentPoint.recordedAtMillis - previousPoint.recordedAtMillis).toDouble() /
                MILLIS_PER_SECOND
        if (elapsedSeconds <= 0.0) {
            return 0f
        }

        return (distanceBetweenMeters(previousPoint, currentPoint) / elapsedSeconds).toFloat()
    }

    private fun distanceBetweenMeters(
        start: TripTrackingPoint,
        end: TripTrackingPoint
    ): Double {
        val startLatitude = Math.toRadians(start.latitude)
        val endLatitude = Math.toRadians(end.latitude)
        val deltaLatitude = Math.toRadians(end.latitude - start.latitude)
        val deltaLongitude = Math.toRadians(end.longitude - start.longitude)

        val haversine = sin(deltaLatitude / 2).pow(2.0) +
            cos(startLatitude) * cos(endLatitude) * sin(deltaLongitude / 2).pow(2.0)
        val centralAngle = 2 * atan2(sqrt(haversine), sqrt(1 - haversine))

        return EARTH_RADIUS_METERS * centralAngle
    }

    private companion object {
        private const val EARTH_RADIUS_METERS = 6_371_000.0
        private const val MILLIS_PER_SECOND = 1_000L
        private const val SECONDS_PER_HOUR = 3_600.0
        private const val METERS_PER_KILOMETER = 1_000.0
        private const val METERS_PER_SECOND_TO_KMH = 3.6
    }
}
