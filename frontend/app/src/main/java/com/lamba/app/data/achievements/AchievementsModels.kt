package com.lamba.app.data.achievements

import com.google.gson.annotations.SerializedName

data class AchievementResponse(
    val id: Int,
    val name: String,
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    val unlocked: Boolean = false,
    @SerializedName("unlocked_at")
    val unlockedAt: String? = null
)
