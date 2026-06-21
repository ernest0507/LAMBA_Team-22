package com.lamba.app.data.records

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RecordsApi {
    @POST("api/v1/cars/{car_id}/records")
    suspend fun createRecord(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int,
        @Body request: MaintenanceRecordCreateRequest
    ): MaintenanceRecordResponse

    @GET("api/v1/cars/{car_id}/timeline")
    suspend fun timeline(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int
    ): List<TimelineItemResponse>
}
