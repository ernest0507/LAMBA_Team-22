package com.lamba.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import screens.assistant.AIChatScreenStarter
import screens.assistant.AiChatScreen
import android.net.Uri
import com.lamba.app.screens.home.HomeScreen
import screens.home.TimelineExpensesAndEvents

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "timeline"
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
            HomeScreen()
        }

        composable("timeline") {
            TimelineExpensesAndEvents(
                onBackClick = {
                    navController.navigate("home")
                }
            )
        }
    }
}