package com.lamba.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
<<<<<<< Updated upstream
//import com.lamba.app.navigation.AppNavigation
=======
import com.lamba.app.screens.greeting.CreationDigitalTwinStep1
import com.lamba.app.screens.greeting.CreationDigitalTwinStep2
import com.lamba.app.screens.greeting.GreetingScreen
import com.lamba.app.screens.history.HistoryScreen
import com.lamba.app.screens.profile.ProfileScreen
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LAMBA_MVPv0Theme {
            }
        }
    }
}
