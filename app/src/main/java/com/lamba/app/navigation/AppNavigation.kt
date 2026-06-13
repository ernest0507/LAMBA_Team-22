package com.lamba.app.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.lamba.app.screens.home.HomeScreen
import com.lamba.app.screens.garage.CarDetailsScreen

import screens.assistant.AIChatScreenStarter
import screens.assistant.AiChatScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "ai_starter"
    ) {
        composable("ai_starter") {
            AIChatScreenStarter(
                onQuestionClick = { question ->
                    navController.navigate("ai_chat/${Uri.encode(question)}")
                },
                onNewChatClick = {
                    navController.navigate("ai_chat/When will the next maintenance?")
                },
            )
        }
        composable("ai_chat/{question}") {
            backStackEntry ->
            val question = backStackEntry.arguments?.getString("question") ?: ""
            AiChatScreen(userMessage = question)
        }

        composable("home") {
            HomeScreen(
                onCarClick = {
                    navController.navigate("car_details")
                },
                onQuestionSend = { question ->
                    navController.navigate("ai_chat/${Uri.encode(question)}")
                },
                onAiClick = {
                    navController.navigate("ai_starter")
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
    }
}