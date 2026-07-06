package com.lamba.app.data.trips.tracking

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TripSessionTrackerTest {
    @Test
    fun addPointCalculatesDistanceAndAverageSpeed() {
        val tracker = TripSessionTracker(nowMillis = { 0L })

        tracker.start(startedAtMillis = 0L)
        tracker.addPoint(
            TripTrackingPoint(
                latitude = 0.0,
                longitude = 0.0,
                accuracyMeters = 5f,
                speedMetersPerSecond = null,
                recordedAtMillis = 0L
            )
        )
        val snapshot = tracker.addPoint(
            TripTrackingPoint(
                latitude = 0.0,
                longitude = 0.001,
                accuracyMeters = 5f,
                speedMetersPerSecond = null,
                recordedAtMillis = 10_000L
            )
        )

        assertTrue(snapshot.isTracking)
        assertEquals(10L, snapshot.elapsedSeconds)
        assertEquals(111.2, snapshot.distanceMeters, 1.0)
        assertEquals(40.0, snapshot.averageSpeedKmh, 1.0)
    }

    @Test
    fun addPointUsesProvidedCurrentSpeedAndTracksMaxSpeed() {
        val tracker = TripSessionTracker(nowMillis = { 0L })

        tracker.start(startedAtMillis = 0L)
        val snapshot = tracker.addPoint(
            TripTrackingPoint(
                latitude = 55.75,
                longitude = 37.61,
                accuracyMeters = 4f,
                speedMetersPerSecond = 12.5f,
                recordedAtMillis = 1_000L
            )
        )

        assertEquals(12.5f, snapshot.currentSpeedMetersPerSecond, 0.001f)
        assertEquals(45.0, snapshot.maxSpeedKmh, 0.001)
    }

    @Test
    fun stopReturnsFinalSnapshotAndResetsTracker() {
        val tracker = TripSessionTracker(nowMillis = { 20_000L })

        tracker.start(startedAtMillis = 0L)
        tracker.addPoint(
            TripTrackingPoint(
                latitude = 0.0,
                longitude = 0.0,
                accuracyMeters = null,
                speedMetersPerSecond = null,
                recordedAtMillis = 0L
            )
        )

        val stoppedSnapshot = tracker.stop(stoppedAtMillis = 20_000L)
        val resetSnapshot = tracker.snapshot(atMillis = 21_000L)

        assertFalse(stoppedSnapshot.isTracking)
        assertEquals(20L, stoppedSnapshot.elapsedSeconds)
        assertFalse(resetSnapshot.isTracking)
        assertEquals(0.0, resetSnapshot.distanceMeters, 0.0)
    }
}
