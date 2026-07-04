package com.lamba.app.data.assistant

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class AssistantModelsTest {
    @Test
    fun assistantMessageRequestSerializesChatId() {
        val json = Gson().toJson(
            AssistantMessageRequest(
                carId = 3,
                chatId = 55,
                message = "How much did I spend this month?"
            )
        )

        assertEquals(
            """{"car_id":3,"chat_id":55,"message":"How much did I spend this month?"}""",
            json
        )
    }

    @Test
    fun assistantMessageResponseMapsChatId() {
        val response = Gson().fromJson(
            """
            {
              "assistant_message": "Saved.",
              "action": "message",
              "chat_id": 55
            }
            """.trimIndent(),
            AssistantMessageResponse::class.java
        )

        assertEquals(55, response.chatId)
    }
}
