package com.lamba.app.data.achievements

class AchievementsRepository(
    private val api: AchievementsApi = AchievementsNetwork.api
) {
    suspend fun achievements(accessToken: String, carId: Int): List<AchievementResponse> {
        return api.achievements(
            authorization = "Bearer $accessToken",
            carId = carId
        )
    }
}
