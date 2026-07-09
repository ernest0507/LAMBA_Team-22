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
    private var currentAccessToken: String? = null
    private var currentCarId: Int? = null

    fun loadAchievements(accessToken: String?, carId: Int?) {
        currentAccessToken = accessToken
        currentCarId = carId

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

    fun unlockAchievement(achievementId: Int) {
        val token = currentAccessToken ?: return
        val carId = currentCarId ?: return

        viewModelScope.launch {
            val result = runCatching {
                repository.unlockAchievement(token, carId, achievementId)
            }

            if (result.isSuccess) {
                val updated = result.getOrThrow()
                _uiState.update { state ->
                    state.copy(
                        achievements = state.achievements.map {
                            if (it.id == achievementId) updated else it
                        }
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        achievements = state.achievements.map {
                            if (it.id == achievementId) it.copy(unlocked = true)
                            else it
                        }
                    )
                }
            }
        }
    }

    private fun demoAchievements(): List<AchievementResponse> {
        return listOf(
            AchievementResponse(id = 1, name = "Пожиратель топлива", description = "Пользователь ввел 15+ литров расхода бензина", category = "statistics"),
            AchievementResponse(id = 2, name = "Арт-объект", description = "Нет разницы в пробеге между его обновлениями через месяц", category = "statistics"),
            AchievementResponse(id = 3, name = "Отчаянный", description = "Дата последнего ТО больше года назад", category = "statistics"),
            AchievementResponse(id = 4, name = "Удача 100%", description = "Отсутствуют записи о поломках на протяжении 6 месяцев", category = "statistics"),
            AchievementResponse(id = 5, name = "Вечный двигатель", description = "Полтора года без замены масла", category = "statistics"),
            AchievementResponse(id = 6, name = "Дальнобойщик на минималках", description = "Пробег за год > 45000", category = "statistics"),
            AchievementResponse(id = 7, name = "Старожил колонки", description = "Количество заправок за год превысило 70", category = "statistics"),
            AchievementResponse(id = 8, name = "Не глотай", description = "Заправился от другой машины на дороге", category = "road"),
            AchievementResponse(id = 9, name = "Снежный король", description = "Смог выехать из сугроба", category = "road"),
            AchievementResponse(id = 10, name = "Дорожная тяга", description = "Отбуксировал другой автомобиль", category = "road"),
            AchievementResponse(id = 11, name = "Пленник асфальта", description = "Пришлось вызывать эвакуатор или просить отбуксироваться другого водителя", category = "road"),
            AchievementResponse(id = 12, name = "ДТ-это ведь дорогое топливо?", description = "Залил не тот бензин", category = "road"),
            AchievementResponse(id = 13, name = "Луноход", description = "Наехал на яму, хрустнула подвеска", category = "road"),
            AchievementResponse(id = 14, name = "Кипятильник", description = "Перегрев двигателя", category = "road"),
            AchievementResponse(id = 15, name = "Как дед учил", description = "Завел машину с толкача", category = "repair"),
            AchievementResponse(id = 16, name = "Энергетический вампир", description = "Прикурил аккумулятор от другой машины", category = "repair"),
            AchievementResponse(id = 17, name = "Мастер пит стопа", description = "Сам поставил запаску", category = "repair"),
            AchievementResponse(id = 18, name = "Одноглазый Джо", description = "Заменил фару", category = "repair"),
            AchievementResponse(id = 19, name = "Руки из плеч", description = "Ремонт любой поломки своими руками", category = "repair"),
            AchievementResponse(id = 20, name = "Да это просто детали притираются", description = "Отсутствие ремонта после сообщения о стуке в двигателе", category = "repair")
        )
    }
}
