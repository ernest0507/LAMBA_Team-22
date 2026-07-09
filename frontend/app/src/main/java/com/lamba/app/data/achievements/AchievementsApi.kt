package com.lamba.app.data.achievements

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AchievementsApi {
    @GET("api/v1/cars/{car_id}/achievements")
    suspend fun achievements(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int
    ): List<AchievementResponse>

    @POST("api/v1/cars/{car_id}/achievements/{achievement_id}/unlock")
    suspend fun unlockAchievement(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int,
        @Path("achievement_id") achievementId: Int
    ): AchievementResponse
}
