package com.lamba.app.data.cars

class CarRepository(
    private val api: CarApi = CarNetwork.api
) {
    suspend fun getCars(accessToken: String): List<CarResponse> {
        return api.getCars(authorization = "Bearer $accessToken")
    }

    suspend fun createCar(accessToken: String, draft: CarDraft): CarResponse {
        return api.createCar(
            authorization = "Bearer $accessToken",
            request = draft.toCreateRequest()
        )
    }

    suspend fun updateCar(accessToken: String, carId: Int, request: CarUpdateRequest): CarResponse {
        return api.updateCar(
            authorization = "Bearer $accessToken",
            carId = carId,
            request = request
        )
    }
}
