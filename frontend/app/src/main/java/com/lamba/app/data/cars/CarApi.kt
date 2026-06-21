package com.lamba.app.data.cars

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CarApi {
    @GET("api/v1/cars")
    suspend fun listCars(
        @Header("Authorization") authorization: String
    ): List<CarResponse>

    @POST("api/v1/cars")
    suspend fun createCar(
        @Header("Authorization") authorization: String,
        @Body request: CarCreateRequest
    ): CarResponse
}
