package com.lamba.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import screens.assistant.AIChatScreenStarter
import screens.assistant.AiChatScreen
import screens.garage.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import screens.garage.model.CarProfile
import android.net.Uri
import com.lamba.app.screens.garage.CarDetailsScreen
import com.lamba.app.screens.home.HomeScreen
import screens.home.TimelineExpensesAndEvents

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var profile by remember { mutableStateOf(CarProfile()) }

    NavHost(
        navController = navController,
        startDestination = "greeting"
    ) {
        composable("greeting") {
            GreetingScreen(
                onCreateTwin = { navController.navigate("create_twin") }
            )
        }

        composable("create_twin") {
            CreateTwinStep1(
                initialData = profile,
                onBack = {
                    navController.popBackStack()
                },
                onNext = { data ->
                    profile = data
                    navController.navigate("create_twin2")
                }
            )
        }

        composable("create_twin2") {
            CreateTwinStep2(
                initialData = profile,
                onBack = {
                    navController.popBackStack()
                },
                onCreateTwin = { data ->
                    profile = data
                    navController.navigate("success")
                }
            )
        }

        composable("success") {
            SuccessScreen(
                onGoMain = {
                    navController.navigate("home")
                }
            )
        }

        composable("home") {
            HomeScreen(
                onAiClick = {
                    navController.navigate("ai_starter")
                },
                onQuestionSend = { question ->
                    navController.navigate("ai_chat/${Uri.encode(question)}")
                },
                onCarClick = {
                    navController.navigate("car_details")
                },
                onTimelineClick = {
                    navController.navigate("timeline")
                }
            )
        }

        composable("timeline") {
            TimelineExpensesAndEvents(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("car_details") {
            CarDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("ai_starter") {
            AIChatScreenStarter(
                onQuestionClick = { question ->
                    navController.navigate("ai_chat/${Uri.encode(question)}")
                },
                onNewChatClick = {
                    val question = Uri.encode("Когда следующее техническое обслуживание?")
                    navController.navigate("ai_chat/$question")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("ai_chat/{question}") { backStackEntry ->
            val question = backStackEntry.arguments?.getString("question") ?: ""
            AiChatScreen(
                userMessage = question,
                onBackClick = { navController.navigate("home") }
            )
        }
    }
}
