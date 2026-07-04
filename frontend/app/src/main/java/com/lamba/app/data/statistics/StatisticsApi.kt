package com.lamba.app.data.statistics

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface StatisticsApi {
    @GET("api/v1/cars/{car_id}/statistics")
    suspend fun statistics(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int
    ): CarStatisticsResponse
}
