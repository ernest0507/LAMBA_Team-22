package com.lamba.app.data.statistics

class StatisticsRepository(
    private val api: StatisticsApi = StatisticsNetwork.api
) {
    suspend fun statistics(accessToken: String, carId: Int): CarStatisticsResponse {
        return api.statistics(
            authorization = "Bearer $accessToken",
            carId = carId
        )
    }
}
