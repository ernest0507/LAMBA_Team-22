package com.lamba.app.data.records

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RecordsApi {
    @POST("api/v1/cars/{car_id}/records")
    suspend fun createRecord(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int,
        @Body request: MaintenanceRecordCreateRequest
    ): MaintenanceRecordResponse

    @Multipart
    @POST("api/v1/cars/{car_id}/records/{record_id}/photos")
    suspend fun uploadRecordPhotos(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int,
        @Path("record_id") recordId: Int,
        @Part files: List<MultipartBody.Part>
    ): List<RecordPhotoResponse>

    @GET("api/v1/cars/{car_id}/records/{record_id}/photos")
    suspend fun recordPhotos(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int,
        @Path("record_id") recordId: Int
    ): List<RecordPhotoResponse>

    @GET("api/v1/cars/{car_id}/records/{record_id}/photos/{photo_id}")
    suspend fun downloadRecordPhoto(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int,
        @Path("record_id") recordId: Int,
        @Path("photo_id") photoId: Int
    ): ResponseBody

    @GET("api/v1/cars/{car_id}/timeline")
    suspend fun timeline(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int
    ): List<TimelineItemResponse>
}
