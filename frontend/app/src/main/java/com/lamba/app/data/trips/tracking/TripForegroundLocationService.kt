package com.lamba.app.data.trips.tracking

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.lamba.app.MainActivity
import com.lamba.app.R
import kotlin.math.roundToInt

class TripForegroundLocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val tracker = TripSessionTracker()
    private var isTracking = false
    private val notificationManager: NotificationManager
        get() = getSystemService(NotificationManager::class.java)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach(::handleLocation)
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_TRACKING -> stopTracking()
            ACTION_START_TRACKING, null -> startTracking()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        removeLocationUpdates()
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    private fun startTracking() {
        if (isTracking) {
            notificationManager.notify(NOTIFICATION_ID, buildNotification(tracker.snapshot()))
            return
        }

        if (!hasLocationPermission()) {
            TripTrackingStateStore.updateError("Нужна точная геолокация для расчета расстояния поездки.")
            stopSelf()
            return
        }

        val initialSnapshot = tracker.start()
        isTracking = true
        TripTrackingStateStore.clearPoints()
        TripTrackingStateStore.update(initialSnapshot)
        startForegroundWithLocationType(buildNotification(initialSnapshot))

        val currentLocationToken = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            currentLocationToken.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                handleLocation(location)
            } else {
                TripTrackingStateStore.updateError("Не удалось получить текущую GPS-точку. Проверьте, что геолокация включена.")
            }
        }.addOnFailureListener {
            TripTrackingStateStore.updateError("Ошибка геолокации: ${it.localizedMessage ?: "GPS недоступен"}")
        }

        fusedLocationClient.locationAvailability.addOnSuccessListener { availability ->
            if (!availability.isLocationAvailable) {
                TripTrackingStateStore.updateError("Геолокация сейчас недоступна. Включите GPS и выйдите на открытое место.")
            }
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LOCATION_INTERVAL_MILLIS
        )
            .setMinUpdateIntervalMillis(FASTEST_LOCATION_INTERVAL_MILLIS)
            .setMinUpdateDistanceMeters(MIN_UPDATE_DISTANCE_METERS)
            .setWaitForAccurateLocation(false)
            .build()

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener {
            TripTrackingStateStore.updateError("Не удалось запустить отслеживание геолокации: ${it.localizedMessage ?: "GPS недоступен"}")
        }
    }

    private fun stopTracking() {
        removeLocationUpdates()
        val snapshot = tracker.stop()
        isTracking = false
        TripTrackingStateStore.update(snapshot)
        stopForegroundCompat()
        stopSelf()
    }

    private fun removeLocationUpdates() {
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun handleLocation(location: Location) {
        val point = location.toTripTrackingPoint()
        val update = tracker.addPoint(point)
        if (update.acceptedPoint != null) {
            TripTrackingStateStore.appendPoint(update.acceptedPoint)
        }
        TripTrackingStateStore.update(update.snapshot)
        notificationManager.notify(NOTIFICATION_ID, buildNotification(update.snapshot))
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startForegroundWithLocationType(notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun stopForegroundCompat() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun buildNotification(snapshot: TripTrackingSnapshot): Notification {
        val launchIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val distanceKm = snapshot.distanceMeters / METERS_PER_KILOMETER
        val contentText = "Distance %.2f km | Speed %d km/h | %s".format(
            distanceKm,
            snapshot.currentSpeedKmh.roundToInt(),
            snapshot.elapsedSeconds.formatElapsedTime()
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(contentText)
            .setContentIntent(pendingIntent)
            .setOngoing(snapshot.isTracking)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = CHANNEL_DESCRIPTION
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun Location.toTripTrackingPoint(): TripTrackingPoint {
        return TripTrackingPoint(
            latitude = latitude,
            longitude = longitude,
            accuracyMeters = if (hasAccuracy()) accuracy else null,
            speedMetersPerSecond = if (hasSpeed()) speed else null,
            recordedAtMillis = time
        )
    }

    private fun Long.formatElapsedTime(): String {
        val hours = this / SECONDS_PER_HOUR
        val minutes = (this % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
        val seconds = this % SECONDS_PER_MINUTE

        return if (hours > 0) {
            "%d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
    }

    companion object {
        const val ACTION_START_TRACKING = "com.lamba.app.trips.action.START_TRACKING"
        const val ACTION_STOP_TRACKING = "com.lamba.app.trips.action.STOP_TRACKING"

        private const val CHANNEL_ID = "trip_tracking"
        private const val CHANNEL_NAME = "Trip tracking"
        private const val CHANNEL_DESCRIPTION = "Shows active trip tracking status"
        private const val NOTIFICATION_ID = 230
        private const val NOTIFICATION_TITLE = "Trip tracking active"
        private const val LOCATION_INTERVAL_MILLIS = 1_000L
        private const val FASTEST_LOCATION_INTERVAL_MILLIS = 500L
        private const val MIN_UPDATE_DISTANCE_METERS = 1f
        private const val METERS_PER_KILOMETER = 1_000.0
        private const val SECONDS_PER_MINUTE = 60L
        private const val SECONDS_PER_HOUR = 3_600L

        fun startIntent(context: Context): Intent {
            return Intent(context, TripForegroundLocationService::class.java).apply {
                action = ACTION_START_TRACKING
            }
        }

        fun stopIntent(context: Context): Intent {
            return Intent(context, TripForegroundLocationService::class.java).apply {
                action = ACTION_STOP_TRACKING
            }
        }
    }
}
