package com.lamba.app.data.trips

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TripPointSyncTest {
    @Test
    fun toBatchRequestConvertsCollectedPoints() {
        val batch = TripPointSync.toBatchRequest(
            listOf(
                CollectedTripPoint(
                    latitude = 55.751244,
                    longitude = 37.618423,
                    accuracyMeters = 4.5f,
                    speedMetersPerSecond = 12.5f,
                    recordedAt = "2026-07-06T12:00:00Z"
                )
            )
        )

        val point = batch.points.single()
        assertEquals(55.751244, point.latitude, 0.0)
        assertEquals(37.618423, point.longitude, 0.0)
        assertEquals(4.5, point.accuracyM ?: 0.0, 0.001)
        assertEquals(45.0, point.speedKmh ?: 0.0, 0.001)
        assertEquals("2026-07-06T12:00:00Z", point.recordedAt)
    }

    @Test
    fun toBatchRequestKeepsEmptyPointListEmpty() {
        val batch = TripPointSync.toBatchRequest(emptyList())

        assertTrue(batch.points.isEmpty())
    }
}

