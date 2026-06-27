package com.lamba.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import com.lamba.app.navigation.AppNavigation
import com.lamba.app.screens.home.HomeScreen
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LAMBA_MVPv0Theme {
                Box(modifier = Modifier
                    .fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
