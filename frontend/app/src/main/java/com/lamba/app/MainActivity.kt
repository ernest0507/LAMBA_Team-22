package com.lamba.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.lamba.app.navigation.AppNavigation
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeUiState by themeViewModel.uiState.collectAsState()
            val view = LocalView.current

            SideEffect {
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                window.navigationBarColor = android.graphics.Color.TRANSPARENT

                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = !themeUiState.isDarkTheme
                    isAppearanceLightNavigationBars = !themeUiState.isDarkTheme
                }
            }

            LAMBA_MVPv0Theme(darkTheme = themeUiState.isDarkTheme) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation(
                        currentTheme = themeUiState.appTheme,
                        onThemeSelected = themeViewModel::setTheme
                    )
                }
            }
        }
    }
}
