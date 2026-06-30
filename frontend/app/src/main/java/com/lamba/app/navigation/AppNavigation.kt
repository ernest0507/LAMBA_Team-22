package com.lamba.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lamba.app.common.SuccessScreen
import com.lamba.app.data.assistant.AssistantViewModel
import com.lamba.app.data.auth.AuthViewModel
import com.lamba.app.data.cars.CarDraft
import com.lamba.app.data.cars.CarViewModel
import com.lamba.app.data.records.MaintenanceRecordCreateRequest
import com.lamba.app.data.records.RecordsViewModel
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
import com.lamba.app.screens.profile.ProfileScreen
import com.lamba.app.screens.statistics.StatisticsScreen
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface
import components.BackButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()
    val carViewModel: CarViewModel = viewModel()
    val carState by carViewModel.uiState.collectAsState()
    val recordsViewModel: RecordsViewModel = viewModel()
    val recordsState by recordsViewModel.uiState.collectAsState()
    val assistantViewModel: AssistantViewModel = viewModel()
    val assistantState by assistantViewModel.uiState.collectAsState()
    var carDraft by remember { mutableStateOf<CarDraft?>(null) }
    val currentCarId = carState.currentCar?.id
    val isCheckingCars = authState.isAuthenticated && !carState.hasCompletedCarsCheck
    val routeErrorMessage = authState.errorMessage ?: carState.errorMessage

    LaunchedEffect(authState.accessToken) {
        if (authState.isAuthenticated) {
            carViewModel.loadCars(authState.accessToken)
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

    LaunchedEffect(assistantState.lastResponse?.recordId) {
        val response = assistantState.lastResponse
        if (
            response?.action == "record_created" &&
            response.recordId != null &&
            currentCarId != null
        ) {
            recordsViewModel.loadTimeline(authState.accessToken, currentCarId)
            assistantViewModel.consumeLastResponse()
        }
    }

    NavHost(
        navController = navController,
        startDestination = LambaRoute.Login.path
    ) {
        composable(LambaRoute.Login.path) {
            LoginScreen(
                isLoading = authState.isLoading || isCheckingCars,
                authErrorMessage = routeErrorMessage,
                onLoginClick = authViewModel::login,
                onRegisterClick = {
                    authViewModel.clearError()
                    navController.navigate(LambaRoute.Registration.path)
                }
            ) }

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
            HomeScreen(
                car = carState.currentCar,
                messages = assistantState.messages,
                isAssistantSending = assistantState.isSending,
                onOpenAiChat = {},
                onAddExpensesClick = { navController.navigate(LambaRoute.ChooseRecordType.path) },
                onOpenHistory = { navController.navigate(LambaRoute.History.path) },
                onOpenStatistics = { navController.navigate(LambaRoute.Statistics.path) },
                onOpenDocuments = { navController.navigate(LambaRoute.Documents.path) },
                onOpenProfile = { navController.navigate(LambaRoute.Profile.path) },
                onSendMessage = { message ->
                    assistantViewModel.sendMessage(
                        accessToken = authState.accessToken,
                        carId = currentCarId,
                        message = message
                    )
                }
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
                        request = form.toRecordRequest()
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
                        request = form.toRecordRequest()
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
                        request = form.toRecordRequest()
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
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(LambaRoute.Statistics.path) {
            StatisticsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(LambaRoute.Documents.path) {
            DocumentsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(LambaRoute.Profile.path) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() }
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
}

private fun NavHostController.openDigitalTwinFlow() {
    navigate(LambaRoute.CreateTwinStep1.path) {
        popUpTo(LambaRoute.Login.path) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.openHomeAfterAuthentication() {
    navigate(LambaRoute.Home.path) {
        popUpTo(LambaRoute.Login.path) {
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

private val RecordDisplayDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

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

@Composable
private fun DocumentsScreen(
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LambaCanvas
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LambaCanvas)
                .padding(
                    PaddingValues(
                        start = LambaSpacing.ScreenHorizontal,
                        top = LambaSpacing.ScreenTop,
                        end = LambaSpacing.ScreenHorizontal,
                        bottom = LambaSpacing.ScreenBottom
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(LambaSpacing.CardPadding)
        ) {
            BackButton(onClick = onBackClick)

            Text(
                text = "Документы",
                style = MaterialTheme.typography.titleLarge,
                color = LambaInk
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(LambaRadius.Large),
                colors = CardDefaults.cardColors(containerColor = LambaSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(LambaSpacing.CardPadding),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "СТС, страховка и чеки",
                        style = MaterialTheme.typography.titleMedium,
                        color = LambaInk,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Экран готов для подключения хранилища документов.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LambaInkMuted
                    )
                }
            }
        }
    }
}

private enum class LambaRoute(
    val path: String
) {
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
    Documents("documents"),
    Profile("profile"),
    RecordSuccess("record_success")
}
