package com.lamba.app.data.cars

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.POST

interface CarApi {
    @GET("api/v1/cars")
    suspend fun getCars(
        @Header("Authorization") authorization: String
    ): List<CarResponse>

    @POST("api/v1/cars")
    suspend fun createCar(
        @Header("Authorization") authorization: String,
        @Body request: CarCreateRequest
    ): CarResponse

    @PATCH("api/v1/cars/{car_id}")
    suspend fun updateCar(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int,
        @Body request: CarUpdateRequest
    ): CarResponse
}
