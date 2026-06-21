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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lamba.app.screens.auth.LoginScreen
import com.lamba.app.screens.auth.RegistrationScreen
import com.lamba.app.screens.expenses.AddExpensesScreen
import com.lamba.app.screens.greeting.CreationDigitalTwinStep1
import com.lamba.app.screens.greeting.CreationDigitalTwinStep2
import com.lamba.app.screens.history.HistoryScreen
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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LambaRoute.Login.path
    ) {
        composable(LambaRoute.Login.path) {
            LoginScreen(
                onLoginClick = { navController.openDigitalTwinFlow() },
                onRegisterClick = { navController.navigate(LambaRoute.Registration.path) }
            )
        }

        composable(LambaRoute.Registration.path) {
            RegistrationScreen(
                onCreateAccountClick = { navController.openLoginAfterRegistration() },
                onLoginClick = { navController.popBackStack() }
            )
        }

        composable(LambaRoute.CreateTwinStep1.path) {
            CreationDigitalTwinStep1(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(LambaRoute.CreateTwinStep2.path) }
            )
        }

        composable(LambaRoute.CreateTwinStep2.path) {
            CreationDigitalTwinStep2(
                onBack = { navController.popBackStack() },
                onCreateTwin = { navController.openHome() }
            )
        }

        composable(LambaRoute.Home.path) {
            HomeScreen(
                onOpenAiChat = {},
                onAddExpensesClick = { navController.navigate(LambaRoute.AddExpenses.path) },
                onOpenHistory = { navController.navigate(LambaRoute.History.path) },
                onOpenStatistics = { navController.navigate(LambaRoute.Statistics.path) },
                onOpenDocuments = { navController.navigate(LambaRoute.Documents.path) },
                onOpenProfile = { navController.navigate(LambaRoute.Profile.path) }
            )
        }

        composable(LambaRoute.AddExpenses.path) {
            AddExpensesScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }

        composable(LambaRoute.History.path) {
            HistoryScreen(
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

private fun NavHostController.openLoginAfterRegistration() {
    navigate(LambaRoute.Login.path) {
        popUpTo(LambaRoute.Registration.path) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.openHome() {
    navigate(LambaRoute.Home.path) {
        popUpTo(LambaRoute.CreateTwinStep1.path) {
            inclusive = true
        }
        launchSingleTop = true
    }
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
    AddExpenses("add_expenses"),
    History("history"),
    Statistics("statistics"),
    Documents("documents"),
    Profile("profile")
}
