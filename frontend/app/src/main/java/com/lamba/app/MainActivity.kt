package com.lamba.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lamba.app.screens.expenses.AddExpensesScreen
import com.lamba.app.screens.home.HomeScreen
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LAMBA_MVPv0Theme {
                AppContent()
            }
        }
    }
}

@Composable
private fun AppContent() {
    var currentScreen by remember { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> HomeScreen(
            onAddExpensesClick = { currentScreen = "add_expenses" }
        )

        "add_expenses" -> AddExpensesScreen(
            onBack = { currentScreen = "home" },
            onSave = { expense ->
                currentScreen = "home"
            }
        )
    }
}
