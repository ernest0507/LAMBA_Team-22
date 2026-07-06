package com.lamba.app.data.trips

class TripRepository(
    private val api: TripApi = TripNetwork.api
) {
    suspend fun startTrip(
        accessToken: String,
        carId: Int,
        startedAt: String? = null
    ): TripResponse {
        return api.startTrip(
            authorization = "Bearer $accessToken",
            carId = carId,
            request = TripStartRequest(startedAt = startedAt)
        )
    }

    suspend fun syncPoints(
        accessToken: String,
        carId: Int,
        tripId: Int,
        points: List<CollectedTripPoint>
    ): List<TripPointResponse> {
        if (points.isEmpty()) {
            return emptyList()
        }

        return api.appendPoints(
            authorization = "Bearer $accessToken",
            carId = carId,
            tripId = tripId,
            request = TripPointSync.toBatchRequest(points)
        )
    }

    suspend fun finishTrip(
        accessToken: String,
        carId: Int,
        tripId: Int,
        endedAt: String? = null
    ): TripResponse {
        return api.finishTrip(
            authorization = "Bearer $accessToken",
            carId = carId,
            tripId = tripId,
            request = TripFinishRequest(endedAt = endedAt)
        )
    }

    suspend fun activeTrip(accessToken: String, carId: Int): TripResponse {
        return api.activeTrip(
            authorization = "Bearer $accessToken",
            carId = carId
        )
    }

    suspend fun trips(accessToken: String, carId: Int): List<TripResponse> {
        return api.trips(
            authorization = "Bearer $accessToken",
            carId = carId
        )
    }

    suspend fun tripDetails(
        accessToken: String,
        carId: Int,
        tripId: Int
    ): TripResponse {
        return api.tripDetails(
            authorization = "Bearer $accessToken",
            carId = carId,
            tripId = tripId
        )
    }
}

