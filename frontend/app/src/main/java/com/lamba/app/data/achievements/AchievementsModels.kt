package com.lamba.app.data.achievements

import com.google.gson.annotations.SerializedName

data class AchievementResponse(
    val id: Int,
    val name: String,
    val description: String,
    val category: String = "statistics",
    @SerializedName("image_url")
    val imageUrl: String? = null,
    val unlocked: Boolean = false,
    @SerializedName("unlocked_at")
    val unlockedAt: String? = null
)

internal val CategoryTitles = mapOf(
    "statistics" to "Статистика",
    "road" to "Дорога",
    "repair" to "Ремонт"
)
