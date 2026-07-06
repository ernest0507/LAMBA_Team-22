package com.lamba.app.data.trips.tracking

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TripTrackingStateStore {
    private val _snapshots = MutableStateFlow(TripTrackingSnapshot.Idle)
    val snapshots: StateFlow<TripTrackingSnapshot> = _snapshots.asStateFlow()

    fun snapshot(): TripTrackingSnapshot = _snapshots.value

    fun update(snapshot: TripTrackingSnapshot) {
        _snapshots.value = snapshot
    }

    fun clear() {
        _snapshots.value = TripTrackingSnapshot.Idle
    }
}
