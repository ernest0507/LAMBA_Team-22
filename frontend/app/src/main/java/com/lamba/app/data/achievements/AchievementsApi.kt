package com.lamba.app.data.achievements

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AchievementsApi {
    @GET("api/v1/cars/{car_id}/achievements")
    suspend fun achievements(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int
    ): List<AchievementResponse>
}
