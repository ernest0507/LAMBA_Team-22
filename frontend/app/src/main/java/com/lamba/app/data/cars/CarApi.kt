package com.lamba.app.data.cars

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CarApi {
    @POST("api/v1/cars")
    suspend fun createCar(
        @Header("Authorization") authorization: String,
        @Body request: CarCreateRequest
    ): CarResponse
}
