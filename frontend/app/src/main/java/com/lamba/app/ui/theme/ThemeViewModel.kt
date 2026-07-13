package com.lamba.app.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ThemeUiState(
    val appTheme: AppTheme = AppTheme.LIGHT
) {
    val isDarkTheme: Boolean
        get() = appTheme == AppTheme.DARK
}

class ThemeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ThemeUiState())
    val uiState: StateFlow<ThemeUiState> = _uiState.asStateFlow()

    fun setTheme(theme: AppTheme) {
        _uiState.update { currentState ->
            currentState.copy(appTheme = theme)
        }
    }
}
