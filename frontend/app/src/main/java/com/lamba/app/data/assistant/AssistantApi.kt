package com.lamba.app.data.assistant

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AssistantApi {
    @POST("api/v1/cars/{car_id}/assistant/messages")
    suspend fun sendMessage(
        @Header("Authorization") authorization: String,
        @Path("car_id") carId: Int,
        @Body request: AssistantMessageRequest
    ): AssistantMessageResponse
}
