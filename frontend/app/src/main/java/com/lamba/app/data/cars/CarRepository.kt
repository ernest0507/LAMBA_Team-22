package com.lamba.app.data.cars

class CarRepository(
    private val api: CarApi = CarNetwork.api
) {
    suspend fun createCar(accessToken: String, draft: CarDraft): CarResponse {
        return api.createCar(
            authorization = "Bearer $accessToken",
            request = draft.toCreateRequest()
        )
    }
}
