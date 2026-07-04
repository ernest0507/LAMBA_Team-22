package com.lamba.app.data.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class StatisticsDataUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val statistics: CarStatisticsResponse? = null
)

class StatisticsViewModel(
    private val repository: StatisticsRepository = StatisticsRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatisticsDataUiState())
    val uiState: StateFlow<StatisticsDataUiState> = _uiState.asStateFlow()

    fun loadStatistics(accessToken: String?, carId: Int?) {
        if (accessToken.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Sign in before viewing statistics.")
            }
            return
        }

        if (carId == null) {
            _uiState.update {
                it.copy(errorMessage = "Create a digital twin before viewing statistics.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            runCatching {
                repository.statistics(accessToken, carId)
            }.onSuccess { statistics ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        statistics = statistics,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toStatisticsMessage()
                    )
                }
            }
        }
    }

    private fun Throwable.toStatisticsMessage(): String {
        return when (this) {
            is HttpException -> when (code()) {
                401 -> "Session expired. Sign in again."
                404 -> "Car was not found. Create a digital twin first."
                else -> "Backend error: HTTP ${code()}."
            }
            is IOException -> "Cannot reach backend. Start the backend and check the base URL."
            else -> message ?: "Statistics request failed."
        }
    }
}
