package com.lamba.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lamba.app.data.auth.AuthViewModel
import com.lamba.app.screens.auth.LoginScreen
import com.lamba.app.screens.auth.RegistrationScreen
import com.lamba.app.screens.greeting.GreetingScreen
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LAMBA_MVPv0Theme {
                val authViewModel: AuthViewModel = viewModel()
                val authState by authViewModel.uiState.collectAsState()
                var authMode by rememberSaveable { mutableStateOf(AuthMode.Register) }

                when {
                    authState.isAuthenticated -> GreetingScreen()
                    authMode == AuthMode.Login -> LoginScreen(
                        isLoading = authState.isLoading,
                        authErrorMessage = authState.errorMessage,
                        onLoginClick = authViewModel::login,
                        onRegisterClick = {
                            authViewModel.clearError()
                            authMode = AuthMode.Register
                        }
                    )
                    else -> RegistrationScreen(
                        isLoading = authState.isLoading,
                        authErrorMessage = authState.errorMessage,
                        onCreateAccountClick = authViewModel::register,
                        onLoginClick = {
                            authViewModel.clearError()
                            authMode = AuthMode.Login
                        }
                    )
                }
            }
        }
    }
}

private enum class AuthMode {
    Register,
    Login
}
