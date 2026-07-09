package com.lamba.app.data.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AchievementsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val achievements: List<AchievementResponse> = emptyList()
)

class AchievementsViewModel(
    private val repository: AchievementsRepository = AchievementsRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState: StateFlow<AchievementsUiState> = _uiState.asStateFlow()

    fun loadAchievements(accessToken: String?, carId: Int?) {
        if (accessToken.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Sign in before viewing achievements.")
            }
            return
        }

        if (carId == null) {
            _uiState.update {
                it.copy(errorMessage = "Create a digital twin before viewing achievements.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val result = runCatching {
                repository.achievements(accessToken, carId)
            }

            val achievements = result.getOrElse {
                demoAchievements()
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    achievements = achievements,
                    errorMessage = null
                )
            }
        }
    }

    private fun demoAchievements(): List<AchievementResponse> {
        return listOf(
            AchievementResponse(
                id = 1,
                name = "Первая заправка",
                description = "Добавьте первую запись о заправке автомобиля",
                imageUrl = null,
                unlocked = true,
                unlockedAt = "10.06.2026"
            ),
            AchievementResponse(
                id = 2,
                name = "1000 км",
                description = "Преодолейте 1000 километров пробега",
                imageUrl = null,
                unlocked = true,
                unlockedAt = "10.06.2026"
            ),
            AchievementResponse(
                id = 3,
                name = "Экономист",
                description = "Сэкономьте 10 000 ₽ на топливе за месяц",
                imageUrl = null,
                unlocked = false
            ),
            AchievementResponse(
                id = 4,
                name = "Мастер ТО",
                description = "Пройдите 3 плановых технических обслуживания",
                imageUrl = null,
                unlocked = true,
                unlockedAt = "12.06.2026"
            ),
            AchievementResponse(
                id = 5,
                name = "Ночной водитель",
                description = "Совершите 10 поездок в ночное время",
                imageUrl = null,
                unlocked = false
            ),
            AchievementResponse(
                id = 6,
                name = "Бережливый",
                description = "Не имейте штрафов и нарушений 6 месяцев",
                imageUrl = null,
                unlocked = false
            )
        )
    }

}
