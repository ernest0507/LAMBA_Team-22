package com.lamba.app.data.trips

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface TripApi {
    @POST("api/v1/cars/{car_id}/trips/start")
    suspend fun startTrip(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int,
        @Body request: TripStartRequest
    ): TripResponse

    @POST("api/v1/trips/{trip_id}/points")
    suspend fun appendPoints(
        @Header("Authorization") authorization: String,
        @Path("trip_id") tripId: Int,
        @Body request: TripPointBatchRequest
    ): List<TripPointResponse>

    @POST("api/v1/trips/{trip_id}/finish")
    suspend fun finishTrip(
        @Header("Authorization") authorization: String,
        @Path("trip_id") tripId: Int,
        @Body request: TripFinishRequest
    ): TripResponse

    @GET("api/v1/cars/{car_id}/trips/active")
    suspend fun activeTrip(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int
    ): TripResponse?

    @GET("api/v1/cars/{car_id}/trips")
    suspend fun trips(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int
    ): List<TripResponse>

    @GET("api/v1/trips/{trip_id}")
    suspend fun tripDetails(
        @Header("Authorization") authorization: String,
        @Path("trip_id") tripId: Int
    ): TripResponse
}

