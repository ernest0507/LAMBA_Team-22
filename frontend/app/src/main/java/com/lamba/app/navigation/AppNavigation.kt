package com.lamba.app.navigation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.lamba.app.R
import com.lamba.app.common.LoadingOverlay
import com.lamba.app.common.SuccessScreen
import com.lamba.app.data.achievements.AchievementsViewModel
import com.lamba.app.data.assistant.AssistantViewModel
import com.lamba.app.data.auth.AuthViewModel
import com.lamba.app.data.cars.CarDraft
import com.lamba.app.data.cars.CarViewModel
import com.lamba.app.data.records.MaintenanceRecordCreateRequest
import com.lamba.app.data.records.RecordsViewModel
import com.lamba.app.data.statistics.StatisticsViewModel
import com.lamba.app.data.trips.CollectedTripPoint
import com.lamba.app.data.trips.TripRepository
import com.lamba.app.data.trips.TripResponse
import com.lamba.app.data.trips.tracking.TripForegroundLocationService
import com.lamba.app.data.trips.tracking.TripTrackingPoint
import com.lamba.app.data.trips.tracking.TripTrackingStateStore
import com.lamba.app.screens.achievements.AchievementsScreen
import com.lamba.app.screens.auth.LoginScreen
import com.lamba.app.screens.auth.RegistrationScreen
import com.lamba.app.screens.greeting.CreationDigitalTwinStep1
import com.lamba.app.screens.greeting.CreationDigitalTwinStep2
import com.lamba.app.screens.history.ChooseRecordTypeScreen
import com.lamba.app.screens.history.ExpensesRecordFormData
import com.lamba.app.screens.history.ExpensesRecordScreen
import com.lamba.app.screens.history.HistoryScreen
import com.lamba.app.screens.history.MaintenanceRecordFormData
import com.lamba.app.screens.history.MaintenanceRecordScreen
import com.lamba.app.screens.history.RecordType
import com.lamba.app.screens.history.RepairRecordFormData
import com.lamba.app.screens.history.RepairRecordScreen
import com.lamba.app.screens.home.HomeScreen
import com.lamba.app.screens.profile.AppSettingsScreen
import com.lamba.app.screens.profile.ProfileScreen
import com.lamba.app.screens.profile.VehicleDataScreen
import com.lamba.app.screens.profile.toVehicleDataUiModel
import com.lamba.app.screens.statistics.StatisticsScreen
import com.lamba.app.screens.trip.TripFinishedScreen
import com.lamba.app.screens.trip.TripHistoryScreen
import com.lamba.app.ui.theme.AppTheme
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaSurface
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun AppNavigation(
    currentTheme: AppTheme = AppTheme.LIGHT,
    onThemeSelected: (AppTheme) -> Unit = {}
) {
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()

    if (authState.isRestoringSession) {
        SessionRestoreScreen()
        return
    }

    val navController = rememberNavController()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val carViewModel: CarViewModel = viewModel()
    val carState by carViewModel.uiState.collectAsState()
    val recordsViewModel: RecordsViewModel = viewModel()
    val recordsState by recordsViewModel.uiState.collectAsState()
    val statisticsViewModel: StatisticsViewModel = viewModel()
    val statisticsState by statisticsViewModel.uiState.collectAsState()
    val achievementsViewModel: AchievementsViewModel = viewModel()
    val achievementsState by achievementsViewModel.uiState.collectAsState()
    val assistantViewModel: AssistantViewModel = viewModel()
    val assistantState by assistantViewModel.uiState.collectAsState()
    val tripTrackingSnapshot by TripTrackingStateStore.snapshots.collectAsState()
    val tripTrackingPoints by TripTrackingStateStore.points.collectAsState()
    val tripTrackingErrorMessage by TripTrackingStateStore.errorMessage.collectAsState()
    val tripRepository = remember { TripRepository() }
    val scanner = remember { GmsBarcodeScanning.getClient(context) }

    var carDraft by remember { mutableStateOf<CarDraft?>(null) }
    var scannedQrValue by remember { mutableStateOf<String?>(null) }
    val currentCarId = carState.currentCar?.id
    val isCheckingCars = authState.isAuthenticated && !carState.hasCompletedCarsCheck
    val routeErrorMessage = authState.errorMessage ?: carState.errorMessage
    val hasExpiredSessionError = listOf(
        carState.errorMessage,
        recordsState.errorMessage,
        statisticsState.errorMessage,
        achievementsState.errorMessage,
        assistantState.errorMessage
    ).any { it.isSessionExpiredMessage() }
    var isTripActive by remember { mutableStateOf(false) }
    var tripStartedAtMillis by remember { mutableStateOf<Long?>(null) }
    var activeTripId by remember { mutableStateOf<Int?>(null) }
    var finishedTripDurationMillis by remember { mutableStateOf(0L) }
    var finishedTripDistanceKm by remember { mutableStateOf(0.0) }
    var finishedTripAverageSpeedKmH by remember { mutableStateOf(0.0) }
    var finishedTripFuelConsumptionL by remember { mutableStateOf(0.0) }
    var locationErrorDialogMessage by remember { mutableStateOf<String?>(null) }
    var locationDialogAction by remember { mutableStateOf<TripLocationDialogAction?>(null) }
    var shouldStartTripAfterPermission by remember { mutableStateOf(false) }
    var tripHistoryItems by remember { mutableStateOf<List<TripResponse>>(emptyList()) }
    var isTripHistoryLoading by remember { mutableStateOf(false) }
    var tripHistoryErrorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authState.sessionExpired) {
        if (authState.sessionExpired) {
            context.stopTripTrackingService()
            TripTrackingStateStore.clear()
            carViewModel.clearSession()
            navController.openLoginAfterLogout()
        }
    }

    LaunchedEffect(hasExpiredSessionError) {
        if (hasExpiredSessionError) {
            authViewModel.expireSession()
        }
    }

    fun showLocationError(message: String, action: TripLocationDialogAction? = null) {
        locationErrorDialogMessage = message
        locationDialogAction = action
    }

    fun startTrip(accessToken: String, carId: Int) {
        if (!context.isDeviceLocationEnabled()) {
            showLocationError(
                message = "Геолокация выключена на устройстве. Включите Location/GPS и попробуйте снова.",
                action = TripLocationDialogAction.OpenLocationSettings
            )
            return
        }

        coroutineScope.launch {
            runCatching {
                tripRepository.startTrip(
                    accessToken = accessToken,
                    carId = carId,
                    startedAt = Instant.now().toString()
                )
            }.onSuccess { startedTrip ->
                isTripActive = true
                activeTripId = startedTrip.id
                tripStartedAtMillis =
                    startedTrip.startedAt.toEpochMillisOrNull() ?: System.currentTimeMillis()
                locationErrorDialogMessage = null
                TripTrackingStateStore.updateError(null)
                context.startTripTrackingService()
            }.onFailure { error ->
                if (error is HttpException && error.code() == 409) {
                    runCatching {
                        tripRepository.activeTrip(accessToken, carId)
                    }.onSuccess { activeTrip ->
                        if (activeTrip != null) {
                            isTripActive = true
                            activeTripId = activeTrip.id
                            tripStartedAtMillis =
                                activeTrip.startedAt.toEpochMillisOrNull() ?: System.currentTimeMillis()
                            locationErrorDialogMessage = null
                            TripTrackingStateStore.updateError(null)
                            context.startTripTrackingService()
                        } else {
                            locationErrorDialogMessage =
                                "На сервере уже есть активная поездка, но приложение не смогло получить ее данные. Попробуйте еще раз."
                        }
                    }.onFailure {
                        locationErrorDialogMessage =
                            "На сервере уже есть активная поездка, но приложение не смогло подключиться к ней. Проверьте интернет и попробуйте еще раз."
                    }
                } else {
                    locationErrorDialogMessage =
                        "Не удалось начать поездку. Проверьте соединение с сервером."
                }
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val hasPreciseLocationPermission =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

        val accessToken = authState.accessToken
        val carId = currentCarId
        val shouldStartTrip = shouldStartTripAfterPermission
        shouldStartTripAfterPermission = false

        if (
            hasPreciseLocationPermission &&
            shouldStartTrip &&
            !accessToken.isNullOrBlank() &&
            carId != null &&
            !isTripActive
        ) {
            startTrip(accessToken, carId)
        } else if (!hasPreciseLocationPermission) {
            val canAskAgain = context.findActivity()?.let { activity ->
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } ?: false

            showLocationError(
                message = "Для поездки нужна точная геолокация. Разрешите Precise location и попробуйте снова.",
                action = if (canAskAgain) {
                    TripLocationDialogAction.RequestPermission
                } else {
                    TripLocationDialogAction.OpenAppSettings
                }
            )
        }
    }

    fun updateFinishedTripStats(trip: TripResponse) {
        finishedTripDurationMillis = trip.durationSeconds * 1000L
        finishedTripDistanceKm = trip.distanceM.toDoubleOrNull()?.div(1000.0) ?: 0.0
        finishedTripAverageSpeedKmH = trip.averageSpeedKmh.toDoubleOrNull() ?: 0.0
        finishedTripFuelConsumptionL = 0.0
    }

    fun launchLocationPermissionRequest(startTripAfterGrant: Boolean) {
        shouldStartTripAfterPermission = startTripAfterGrant
        val permissions = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && startTripAfterGrant) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()

        locationPermissionLauncher.launch(permissions)
    }

    fun requestTripPermissions() {
        if (!context.isDeviceLocationEnabled()) {
            showLocationError(
                message = "Геолокация выключена на устройстве. Включите Location/GPS и попробуйте снова.",
                action = TripLocationDialogAction.OpenLocationSettings
            )
            return
        }

        launchLocationPermissionRequest(startTripAfterGrant = true)
    }

    LaunchedEffect(authState.accessToken, currentCarId) {
        isTripActive = false
        activeTripId = null
        tripStartedAtMillis = null
        context.stopTripTrackingService()
        TripTrackingStateStore.clear()
    }

    LaunchedEffect(authState.accessToken) {
        if (authState.isAuthenticated) {
            carViewModel.loadCars(authState.accessToken)
        }
    }

    LaunchedEffect(authState.isAuthenticated, currentCarId) {
        if (
            authState.isAuthenticated &&
            currentCarId != null &&
            !context.hasTripLocationPermission()
        ) {
            launchLocationPermissionRequest(startTripAfterGrant = false)
        }
    }

    LaunchedEffect(tripTrackingErrorMessage) {
        if (!tripTrackingErrorMessage.isNullOrBlank()) {
            locationErrorDialogMessage = tripTrackingErrorMessage
        }
    }

    LaunchedEffect(isTripActive, tripTrackingPoints.size) {
        if (isTripActive && tripTrackingPoints.size < 2) {
            delay(15_000L)
            if (isTripActive && TripTrackingStateStore.pointsSnapshot().size < 2) {
                locationErrorDialogMessage =
                    "Геолокация не отчитывается: за 15 секунд не пришли GPS-точки для расчета расстояния. Проверьте точную геолокацию, включенный GPS и попробуйте выйти на открытое место."
            }
        }
    }

    LaunchedEffect(
        authState.isAuthenticated,
        carState.hasCompletedCarsCheck,
        carState.hasExistingCar,
        carState.createdCar?.id
    ) {
        if (
            authState.isAuthenticated &&
            carState.hasCompletedCarsCheck &&
            carState.createdCar == null
        ) {
            if (carState.hasExistingCar) {
                navController.openHomeAfterAuthentication()
            } else {
                navController.openDigitalTwinFlow()
            }
        }
    }

    LaunchedEffect(carState.createdCar?.id) {
        if (carState.createdCar != null) {
            navController.openHomeAfterCarCreation()
        }
    }

    LaunchedEffect(recordsState.createdRecord?.id) {
        val createdRecord = recordsState.createdRecord
        if (createdRecord != null) {
            recordsViewModel.loadTimeline(authState.accessToken, createdRecord.carId)
            recordsViewModel.consumeCreatedRecord()
            navController.navigate(LambaRoute.RecordSuccess.path)
        }
    }

    LaunchedEffect(
        assistantState.lastResponse?.action,
        assistantState.lastResponse?.recordId,
        assistantState.lastResponse?.mileageUpdate?.currentMileageKm
    ) {
        val response = assistantState.lastResponse
        if (
            response?.action == "record_created" &&
            response.recordId != null &&
            currentCarId != null
        ) {
            recordsViewModel.loadTimeline(authState.accessToken, currentCarId)
            assistantViewModel.consumeLastResponse()
        }
        if (
            response?.action == "mileage_updated" &&
            currentCarId != null
        ) {
            carViewModel.loadCars(authState.accessToken)
            assistantViewModel.consumeLastResponse()
        }
    }

    NavHost(
        navController = navController,
        startDestination = LambaRoute.SessionRestore.path
    ) {
        composable(LambaRoute.SessionRestore.path) {
            SessionRestoreScreen()

            LaunchedEffect(
                authState.isRestoringSession,
                authState.isAuthenticated
            ) {
                if (authState.isRestoringSession) {
                    return@LaunchedEffect
                }

                if (!authState.isAuthenticated) {
                    navController.navigate(LambaRoute.Login.path) {
                        popUpTo(LambaRoute.SessionRestore.path) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    return@LaunchedEffect
                }

                navController.openHomeAfterAuthentication()
            }
        }

        composable(LambaRoute.Login.path) {
            LoginScreen(
                isLoading = authState.isLoading,
                authErrorMessage = routeErrorMessage,
                onLoginClick = authViewModel::login,
                onRegisterClick = {
                    authViewModel.clearError()
                    navController.navigate(LambaRoute.Registration.path)
                }
            )
        }

        composable(LambaRoute.Registration.path) {
            RegistrationScreen(
                isLoading = authState.isLoading || isCheckingCars,
                authErrorMessage = routeErrorMessage,
                onCreateAccountClick = authViewModel::register,
                onLoginClick = {
                    authViewModel.clearError()
                    navController.popBackStack()
                }
            )
        }

        composable(LambaRoute.CreateTwinStep1.path) {
            CreationDigitalTwinStep1(
                onBack = { navController.popBackStack() },
                onContinue = { draft ->
                    carDraft = draft
                    carViewModel.clearStatus()
                    navController.navigate(LambaRoute.CreateTwinStep2.path)
                }
            )
        }

        composable(LambaRoute.CreateTwinStep2.path) {
            CreationDigitalTwinStep2(
                onBack = { navController.popBackStack() },
                isLoading = carState.isLoading,
                carErrorMessage = carState.errorMessage,
                onCreateTwin = { color, bodyType ->
                    carViewModel.createCar(
                        accessToken = authState.accessToken,
                        draft = carDraft?.copy(
                            color = color,
                            bodyType = bodyType
                        )
                    )
                }
            )
        }

        composable(LambaRoute.Home.path) {
            Box(modifier = Modifier.fillMaxSize()) {
                HomeScreen(
                    car = carState.currentCar,
                    messages = assistantState.messages,
                    isAssistantSending = assistantState.isSending,
                    onOpenAiChat = {},
                    onAddExpensesClick = { navController.navigate(LambaRoute.ChooseRecordType.path) },
                    onOpenHistory = { navController.navigate(LambaRoute.History.path) },
                    onOpenTripHistory = { navController.navigate(LambaRoute.TripHistory.path) },
                    onOpenStatistics = { navController.navigate(LambaRoute.Statistics.path) },
                    onOpenAchievements = { navController.navigate(LambaRoute.Achievements.path) },
                    onOpenQrClick = qrClick@{
                        if (currentCarId == null) {
                            Toast.makeText(
                                context,
                                "Не нашел машину текущего аккаунта. Обновляю список машин.",
                                Toast.LENGTH_LONG
                            ).show()
                            carViewModel.loadCars(authState.accessToken)
                            return@qrClick
                        }

                        scanner.startScan()
                            .addOnSuccessListener { barcode ->
                                val qrValue = barcode.rawValue
                                if (qrValue.isNullOrBlank()) {
                                    Toast.makeText(
                                        context,
                                        "QR-код не содержит данных чека.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@addOnSuccessListener
                                }

                                scannedQrValue = qrValue
                                Toast.makeText(
                                    context,
                                    "QR-код считан. Добавляю к машине #$currentCarId...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                recordsViewModel.scanReceipt(
                                    accessToken = authState.accessToken,
                                    carId = currentCarId,
                                    qrRaw = qrValue
                                )
                            }
                            .addOnCanceledListener {
                                Toast.makeText(
                                    context,
                                    "Сканирование отменено.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Не удалось открыть сканер QR.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    },
                    onOpenProfile = { navController.navigate(LambaRoute.Profile.path) },
                    onSendMessage = { message ->
                        assistantViewModel.sendMessage(
                            accessToken = authState.accessToken,
                            carId = currentCarId,
                            message = message
                        )
                    },
                    isTripActive = isTripActive,
                    tripStartedAtMillis = tripStartedAtMillis,
                    tripDistanceKm = tripTrackingSnapshot.distanceMeters / 1000.0,
                    onTripHoldComplete = {
                        val accessToken = authState.accessToken
                        val carId = currentCarId

                        if (!accessToken.isNullOrBlank() && carId != null) {
                            coroutineScope.launch {
                                if (isTripActive) {
                                    val tripId = activeTripId ?: runCatching {
                                        tripRepository.activeTrip(accessToken, carId)?.id
                                    }.getOrNull()

                                    if (tripId == null) {
                                        locationErrorDialogMessage =
                                            "Не удалось завершить поездку: активная поездка не найдена на сервере."
                                        context.stopTripTrackingService()
                                        isTripActive = false
                                        tripStartedAtMillis = null
                                        TripTrackingStateStore.clear()
                                        return@launch
                                    }

                                    val collectedPoints = TripTrackingStateStore.pointsSnapshot()
                                        .map { it.toCollectedTripPoint() }

                                    if (collectedPoints.isEmpty()) {
                                        locationErrorDialogMessage =
                                            "Поездка не завершена: приложение еще не получило GPS-точки для отправки на сервер. Подождите несколько секунд на открытом месте и попробуйте завершить еще раз."
                                        return@launch
                                    }

                                    val pointsSyncResult = runCatching {
                                        tripRepository.syncPoints(
                                            accessToken = accessToken,
                                            carId = carId,
                                            tripId = tripId,
                                            points = collectedPoints
                                        )
                                    }

                                    pointsSyncResult.onFailure { error ->
                                        locationErrorDialogMessage =
                                            "Поездка не завершена: GPS-точки не удалось отправить на сервер. ${error.toTripErrorMessage()}"
                                        return@launch
                                    }

                                    runCatching {
                                        tripRepository.finishTrip(
                                            accessToken = accessToken,
                                            carId = carId,
                                            tripId = tripId,
                                            endedAt = Instant.now().toString()
                                        )
                                    }.onSuccess { finishedTrip ->
                                        updateFinishedTripStats(finishedTrip)
                                        context.stopTripTrackingService()
                                        isTripActive = false
                                        activeTripId = null
                                        tripStartedAtMillis = null
                                        TripTrackingStateStore.clear()
                                        navController.navigate(LambaRoute.TripFinished.path)
                                    }.onFailure { error ->
                                        locationErrorDialogMessage =
                                            "Не удалось завершить поездку. ${error.toTripErrorMessage()}"
                                    }
                                } else {
                                    if (!context.hasTripLocationPermission()) {
                                        requestTripPermissions()
                                        return@launch
                                    }

                                    startTrip(accessToken, carId)
                                }
                            }
                        }
                    }
                )

                if (recordsState.isScanningReceipt) {
                    LoadingOverlay(
                        title = "Обрабатываю чек",
                        message = "Получаю данные по QR-коду и добавляю расход в историю."
                    )
                }
            }
        }

        composable(LambaRoute.TripFinished.path) {
            TripFinishedScreen(
                durationMillis = finishedTripDurationMillis,
                distanceKm = finishedTripDistanceKm,
                averageSpeedKmH = finishedTripAverageSpeedKmH,
                fuelConsumptionL = finishedTripFuelConsumptionL,
                onDoneClick = {
                    navController.navigate(LambaRoute.Home.path) {
                        popUpTo(LambaRoute.Home.path) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(LambaRoute.TripHistory.path) {
            LaunchedEffect(authState.accessToken, currentCarId) {
                val accessToken = authState.accessToken
                val carId = currentCarId

                if (accessToken.isNullOrBlank() || carId == null) {
                    tripHistoryItems = emptyList()
                    tripHistoryErrorMessage = "Автомобиль не выбран."
                    return@LaunchedEffect
                }

                isTripHistoryLoading = true
                tripHistoryErrorMessage = null

                runCatching {
                    tripRepository.trips(accessToken, carId)
                }.onSuccess { trips ->
                    tripHistoryItems = trips.sortedByDescending { it.startedAt }
                }.onFailure { error ->
                    tripHistoryErrorMessage =
                        "Не удалось загрузить историю поездок. ${error.toTripErrorMessage()}"
                }

                isTripHistoryLoading = false
            }

            TripHistoryScreen(
                isLoading = isTripHistoryLoading,
                errorMessage = tripHistoryErrorMessage,
                trips = tripHistoryItems,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(LambaRoute.ChooseRecordType.path) {
            ChooseRecordTypeScreen(
                onBackClick = { navController.popBackStack() },
                onTypeSelected = { type ->
                    when (type) {
                        RecordType.EXPENSE -> navController.navigate(LambaRoute.ExpensesRecord.path)
                        RecordType.MAINTENANCE -> navController.navigate(LambaRoute.AddMaintenance.path)
                        RecordType.BREAKDOWN -> navController.navigate(LambaRoute.AddBreakdown.path)
                    }
                }
            )
        }

        composable(LambaRoute.ExpensesRecord.path) {
            ExpensesRecordScreen(
                onBack = { navController.popBackStack() },
                onSave = { form ->
                    recordsViewModel.createRecord(
                        accessToken = authState.accessToken,
                        carId = currentCarId,
                        request = form.toRecordRequest(),
                        imageUris = form.imageUris,
                        contentResolver = context.contentResolver
                    )
                },
                isSaving = recordsState.isSaving,
                errorMessage = recordsState.errorMessage
            )
        }

        composable(LambaRoute.AddMaintenance.path) {
            MaintenanceRecordScreen(
                onBack = { navController.popBackStack() },
                onSave = { form ->
                    recordsViewModel.createRecord(
                        accessToken = authState.accessToken,
                        carId = currentCarId,
                        request = form.toRecordRequest(),
                        imageUris = form.imageUris,
                        contentResolver = context.contentResolver
                    )
                },
                isSaving = recordsState.isSaving,
                errorMessage = recordsState.errorMessage
            )
        }

        composable(LambaRoute.AddBreakdown.path) {
            RepairRecordScreen(
                onBack = { navController.popBackStack() },
                onSave = { form ->
                    recordsViewModel.createRecord(
                        accessToken = authState.accessToken,
                        carId = currentCarId,
                        request = form.toRecordRequest(),
                        imageUris = form.imageUris,
                        contentResolver = context.contentResolver
                    )
                },
                isSaving = recordsState.isSaving,
                errorMessage = recordsState.errorMessage
            )
        }

        composable(LambaRoute.History.path) {
            LaunchedEffect(authState.accessToken, currentCarId) {
                recordsViewModel.loadTimeline(authState.accessToken, currentCarId)
            }

            HistoryScreen(
                isLoading = recordsState.isLoading,
                errorMessage = recordsState.errorMessage,
                records = recordsState.timeline,
                recordPhotos = recordsState.recordPhotos,
                onRecordExpanded = { recordId ->
                    recordsViewModel.loadRecordPhotos(
                        accessToken = authState.accessToken,
                        carId = currentCarId,
                        recordId = recordId
                    )
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(LambaRoute.Statistics.path) {
            LaunchedEffect(authState.accessToken, currentCarId) {
                statisticsViewModel.loadStatistics(authState.accessToken, currentCarId)
            }

            StatisticsScreen(
                isLoading = statisticsState.isLoading,
                errorMessage = statisticsState.errorMessage,
                statistics = statisticsState.statistics,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(LambaRoute.Achievements.path) {
            LaunchedEffect(authState.accessToken, currentCarId) {
                achievementsViewModel.loadAchievements(authState.accessToken, currentCarId)
            }

            AchievementsScreen(
                isLoading = achievementsState.isLoading,
                errorMessage = achievementsState.errorMessage,
                achievements = achievementsState.achievements,
                onBackClick = { navController.popBackStack() },
                onUnlockClick = { achievementId ->
                    achievementsViewModel.unlockAchievement(achievementId)
                }
            )
        }

        composable(LambaRoute.Profile.path) {
            ProfileScreen(
                user = authState.currentUser,
                car = carState.currentCar,
                onBackClick = { navController.popBackStack() },
                onVehicleDataClick = { navController.navigate(LambaRoute.VehicleData.path) },
                onAppSettingsClick = { navController.navigate(LambaRoute.AppSettings.path) }
            )
        }

        composable(LambaRoute.VehicleData.path) {
            VehicleDataScreen(
                onBackClick = { navController.popBackStack() },
                vehicleData = carState.currentCar.toVehicleDataUiModel(),
                isSaving = carState.isLoading,
                saveErrorMessage = carState.errorMessage,
                onSaveVehicleData = { request ->
                    carViewModel.updateCar(
                        accessToken = authState.accessToken,
                        carId = currentCarId,
                        request = request
                    )
                }
            )
        }

        composable(LambaRoute.AppSettings.path) {
            AppSettingsScreen(
                currentTheme = currentTheme,
                onThemeSelected = onThemeSelected,
                onBackClick = { navController.popBackStack() },
                onLogoutConfirmed = {
                    context.stopTripTrackingService()
                    TripTrackingStateStore.clear()
                    carViewModel.clearSession()
                    authViewModel.logout()
                    navController.openLoginAfterLogout()
                }
            )
        }

        composable(LambaRoute.QrSuccess.path) {
            SuccessScreen(
                title = "QR-код сканирован",
                message = "Данные чека отправлены на обработку",
                buttonText = "В главное меню",
                onContinue = {
                    navController.navigate(LambaRoute.Home.path) {
                        popUpTo(LambaRoute.QrSuccess.path) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(LambaRoute.RecordSuccess.path) {
            SuccessScreen(
                title = "Запись добавлена.",
                message = "Данные успешно сохранены",
                buttonText = "Перейти к истории",
                onContinue = {
                    navController.navigate(LambaRoute.History.path) {
                        popUpTo(LambaRoute.ChooseRecordType.path) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }

    locationErrorDialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = {
                locationErrorDialogMessage = null
                locationDialogAction = null
            },
            title = {
                Text(
                    text = "Ошибка поездки",
                    color = LambaInk,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = message,
                    color = LambaInkMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val action = locationDialogAction
                        locationErrorDialogMessage = null
                        locationDialogAction = null

                        when (action) {
                            TripLocationDialogAction.RequestPermission -> requestTripPermissions()
                            TripLocationDialogAction.OpenLocationSettings -> {
                                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            }
                            TripLocationDialogAction.OpenAppSettings -> {
                                context.openApplicationSettings()
                            }
                            null -> Unit
                        }
                    }
                ) {
                    Text(locationDialogAction.confirmButtonText())
                }
            },
            containerColor = LambaSurface
        )
    }
}

@Composable
private fun SessionRestoreScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.lamba_logo_foreground),
                contentDescription = "LAMBA",
                modifier = Modifier.size(width = 240.dp, height = 90.dp)
            )
        }
    }
}

private enum class TripLocationDialogAction {
    RequestPermission,
    OpenLocationSettings,
    OpenAppSettings
}

private fun TripLocationDialogAction?.confirmButtonText(): String {
    return when (this) {
        TripLocationDialogAction.RequestPermission -> "Разрешить"
        TripLocationDialogAction.OpenLocationSettings -> "Открыть настройки"
        TripLocationDialogAction.OpenAppSettings -> "Открыть настройки"
        null -> "Понятно"
    }
}

private fun NavHostController.openDigitalTwinFlow() {
    navigate(LambaRoute.CreateTwinStep1.path) {
        popUpTo(graph.id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.openHomeAfterAuthentication() {
    navigate(LambaRoute.Home.path) {
        popUpTo(graph.id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.openHomeAfterCarCreation() {
    navigate(LambaRoute.Home.path) {
        popUpTo(LambaRoute.CreateTwinStep1.path) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.openLoginAfterLogout() {
    navigate(LambaRoute.Login.path) {
        popUpTo(graph.id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private val RecordDisplayDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy")

private fun ExpensesRecordFormData.toRecordRequest(): MaintenanceRecordCreateRequest {
    val expenseType = category.trim()
    val cleanDescription = description.trim()
    val details = listOfNotNull(
        expenseType.takeIf { it.isNotBlank() }?.let { "Type: $it" },
        cleanDescription.takeIf { it.isNotBlank() }
    ).joinToString(separator = "\n")

    return MaintenanceRecordCreateRequest(
        category = "expense",
        title = name.trim(),
        description = details.takeIf { it.isNotBlank() },
        occurredAt = date.toIsoRecordDateOrNull(),
        costAmount = cost.toRecordCostAmount()
    )
}

private fun MaintenanceRecordFormData.toRecordRequest(): MaintenanceRecordCreateRequest {
    return MaintenanceRecordCreateRequest(
        category = "maintenance",
        title = title.trim(),
        description = description.trim().takeIf { it.isNotBlank() },
        occurredAt = serviceDate.toIsoRecordDateOrNull(),
        mileageKm = mileage.toIntOrNull(),
        costAmount = cost.toRecordCostAmount(),
        vendor = organization.trim().takeIf { it.isNotBlank() }
    )
}

private fun RepairRecordFormData.toRecordRequest(): MaintenanceRecordCreateRequest {
    return MaintenanceRecordCreateRequest(
        category = "repair",
        title = category.trim(),
        description = description.trim().takeIf { it.isNotBlank() },
        occurredAt = breakdownDate.toIsoRecordDateOrNull(),
        mileageKm = mileage.toIntOrNull(),
        costAmount = "0.00"
    )
}

private fun String.toIsoRecordDateOrNull(): String? {
    val cleanDate = trim()
    if (cleanDate.isEmpty()) return null

    return try {
        LocalDate.parse(cleanDate, RecordDisplayDateFormatter).toString()
    } catch (_: DateTimeParseException) {
        cleanDate
    }
}

private fun String.toRecordCostAmount(): String {
    return trim().ifBlank { "0.00" }
}

private fun String.toEpochMillisOrNull(): Long? {
    return runCatching {
        Instant.parse(this).toEpochMilli()
    }.getOrElse {
        runCatching {
            OffsetDateTime.parse(this).toInstant().toEpochMilli()
        }.getOrNull()
    }
}

private fun String?.isSessionExpiredMessage(): Boolean {
    return this?.startsWith("Session expired", ignoreCase = true) == true
}

private fun Context.hasTripLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun Context.isDeviceLocationEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        locationManager.isLocationEnabled
    } else {
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}

private fun Context.startTripTrackingService() {
    ContextCompat.startForegroundService(
        this,
        TripForegroundLocationService.startIntent(this)
    )
}

private fun Context.stopTripTrackingService() {
    startService(TripForegroundLocationService.stopIntent(this))
}

private fun Context.openApplicationSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

private fun TripTrackingPoint.toCollectedTripPoint(): CollectedTripPoint {
    return CollectedTripPoint(
        latitude = latitude,
        longitude = longitude,
        accuracyMeters = accuracyMeters,
        speedMetersPerSecond = speedMetersPerSecond,
        recordedAt = Instant.ofEpochMilli(recordedAtMillis).toString()
    )
}

private fun Throwable.toTripErrorMessage(): String {
    return when (this) {
        is HttpException -> "HTTP ${code()}."
        is IOException -> "Проверьте интернет-соединение."
        else -> localizedMessage ?: "Попробуйте еще раз."
    }
}

private enum class LambaRoute(
    val path: String
) {
    SessionRestore("session_restore"),
    Login("login"),
    Registration("registration"),
    CreateTwinStep1("create_twin_step_1"),
    CreateTwinStep2("create_twin_step_2"),
    Home("home"),
    ChooseRecordType("choose_record_type"),
    ExpensesRecord("expenses_record"),
    AddMaintenance("add_maintenance"),
    AddBreakdown("add_breakdown"),
    History("history"),
    Statistics("statistics"),
    Achievements("achievements"),
    Profile("profile"),
    VehicleData("vehicle_data"),
    AppSettings("app_settings"),
    RecordSuccess("record_success"),
    TripFinished("trip_finished"),
    QrSuccess("qr_success"),
    TripHistory("trip_history")
}
