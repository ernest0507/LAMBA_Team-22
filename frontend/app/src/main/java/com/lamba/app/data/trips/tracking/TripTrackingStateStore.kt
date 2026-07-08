package com.lamba.app.data.trips.tracking

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TripTrackingStateStore {
    private val _snapshots = MutableStateFlow(TripTrackingSnapshot.Idle)
    val snapshots: StateFlow<TripTrackingSnapshot> = _snapshots.asStateFlow()
    private val _points = MutableStateFlow<List<TripTrackingPoint>>(emptyList())
    val points: StateFlow<List<TripTrackingPoint>> = _points.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun snapshot(): TripTrackingSnapshot = _snapshots.value

    fun pointsSnapshot(): List<TripTrackingPoint> = _points.value

    fun update(snapshot: TripTrackingSnapshot) {
        _snapshots.value = snapshot
    }

    fun updateError(message: String?) {
        _errorMessage.value = message
    }

    fun appendPoint(point: TripTrackingPoint) {
        _points.value = _points.value + point
        _errorMessage.value = null
    }

    fun clearPoints() {
        _points.value = emptyList()
    }

    fun clear() {
        _snapshots.value = TripTrackingSnapshot.Idle
        clearPoints()
        updateError(null)
    }
}
