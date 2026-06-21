package com.lamba.app.data.records

import java.time.LocalDate

class RecordsRepository(
    private val api: RecordsApi = RecordsNetwork.api
) {
    suspend fun createExpense(
        accessToken: String,
        carId: Int,
        draft: ExpenseDraft
    ): MaintenanceRecordResponse {
        val cleanDescription = draft.description.trim()
        val title = cleanDescription.ifBlank { "Expense" }

        return api.createRecord(
            authorization = "Bearer $accessToken",
            carId = carId,
            request = MaintenanceRecordCreateRequest(
                title = title,
                description = cleanDescription.takeIf { it.isNotEmpty() },
                occurredAt = LocalDate.now().toString(),
                costAmount = draft.amount.toString()
            )
        )
    }

    suspend fun timeline(accessToken: String, carId: Int): List<TimelineItemResponse> {
        return api.timeline(
            authorization = "Bearer $accessToken",
            carId = carId
        )
    }
}
