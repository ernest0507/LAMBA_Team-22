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
import com.lamba.app.screens.home.HomeScreen
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import screens.garage.CreateTwinStep1
import screens.garage.CreateTwinStep2
import screens.garage.GreetingScreen
import screens.garage.SuccessScreen
import screens.garage.model.CarProfile

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LAMBA_MVPv0Theme {
                CreationFlow()
            }
        }
    }
}

@Composable
private fun CreationFlow() {
    var currentStep by remember { mutableStateOf(0) }
    var profile by remember { mutableStateOf(CarProfile()) }

    when (currentStep) {
        0 -> GreetingScreen(
            onCreateTwin = { currentStep = 1 }
        )

        1 -> CreateTwinStep1(
            initialData = profile,
            onBack = { currentStep = 0 },
            onNext = { data ->
                profile = data
                currentStep = 2
            }
        )

        2 -> CreateTwinStep2(
            initialData = profile,
            onBack = { currentStep = 1 },
            onCreateTwin = {
                currentStep = 3
            }
        )

        3 -> SuccessScreen(
            onGoMain = { currentStep = 4 }
        )

        4 -> HomeScreen()
    }
}
