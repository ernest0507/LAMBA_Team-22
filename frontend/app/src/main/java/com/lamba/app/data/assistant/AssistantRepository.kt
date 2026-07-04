package com.lamba.app.data.assistant

class AssistantRepository(
    private val api: AssistantApi = AssistantNetwork.api
) {
    suspend fun sendMessage(
        accessToken: String,
        carId: Int,
        chatId: Int?,
        message: String
    ): AssistantMessageResponse {
        return api.sendMessage(
            authorization = "Bearer $accessToken",
            carId = carId,
            request = AssistantMessageRequest(
                carId = carId,
                chatId = chatId,
                message = message
            )
        )
    }
}
