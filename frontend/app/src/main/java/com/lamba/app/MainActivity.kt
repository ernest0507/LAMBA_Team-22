package com.lamba.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
//import com.lamba.app.navigation.AppNavigation
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
