package com.lamba.app.data.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class CarUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val createdCar: CarResponse? = null,
    val cars: List<CarResponse> = emptyList()
) {
    val currentCar: CarResponse? = createdCar ?: cars.firstOrNull()
}

class CarViewModel(
    private val repository: CarRepository = CarRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(CarUiState())
    val uiState: StateFlow<CarUiState> = _uiState.asStateFlow()

    fun loadCars(accessToken: String?) {
        if (accessToken.isNullOrBlank()) {
            return
        }

        viewModelScope.launch {
            runCatching {
                repository.listCars(accessToken)
            }.onSuccess { cars ->
                _uiState.update {
                    it.copy(cars = cars, errorMessage = null)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.toCarMessage())
                }
            }
        }
    }

    fun createCar(accessToken: String?, draft: CarDraft?) {
        if (accessToken.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Sign in before creating a digital twin.")
            }
            return
        }

        if (draft == null) {
            _uiState.update {
                it.copy(errorMessage = "Fill in car details before creating a digital twin.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, createdCar = null)
            }

            runCatching {
                repository.createCar(accessToken, draft)
            }.onSuccess { car ->
                _uiState.value = CarUiState(
                    createdCar = car,
                    cars = listOf(car)
                )
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toCarMessage()
                    )
                }
            }
        }
    }

    fun clearStatus() {
        _uiState.value = CarUiState()
    }

    private fun Throwable.toCarMessage(): String {
        return when (this) {
            is HttpException -> when (code()) {
                401 -> "Session expired. Sign in again."
                422 -> "Check the car data and try again."
                else -> "Backend error: HTTP ${code()}."
            }
            is IOException -> "Cannot reach backend. Start the backend and check the base URL."
            else -> message ?: "Digital twin was not created."
        }
    }
}
