package com.lamba.app.data.achievements

import android.content.Context

class AchievementUnlockStore(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun getUnlockedAchievementIds(carId: Int): Set<Int> {
        return preferences.getStringSet(carKey(carId), emptySet()).orEmpty()
            .mapNotNull { it.toIntOrNull() }
            .toSet()
    }

    fun saveUnlockedAchievement(carId: Int, achievementId: Int) {
        val key = carKey(carId)
        val updated = preferences.getStringSet(key, emptySet()).orEmpty()
            .toMutableSet()
            .apply { add(achievementId.toString()) }

        preferences.edit()
            .putStringSet(key, updated)
            .apply()
    }

    private fun carKey(carId: Int): String = "$KEY_UNLOCKED_PREFIX$carId"

    private companion object {
        const val PREFERENCES_NAME = "achievement_unlocks"
        const val KEY_UNLOCKED_PREFIX = "car_"
    }
}
