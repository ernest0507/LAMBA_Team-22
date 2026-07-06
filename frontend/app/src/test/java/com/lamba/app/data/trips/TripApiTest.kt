package com.lamba.app.data.trips

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

class TripApiTest {
    @Test
    fun startTripUsesTripStartRoute() {
        val method = TripApi::class.java.declaredMethods.single {
            it.name == "startTrip"
        }

        val post = method.getAnnotation(POST::class.java)
        assertNotNull(post)
        assertEquals("api/v1/cars/{car_id}/trips", post!!.value)
        assertHasAuthorizationCarAndBody(method.parameterAnnotations)
    }

    @Test
    fun appendPointsUsesTripPointsRoute() {
        val method = TripApi::class.java.declaredMethods.single {
            it.name == "appendPoints"
        }

        val post = method.getAnnotation(POST::class.java)
        assertNotNull(post)
        assertEquals("api/v1/cars/{car_id}/trips/{trip_id}/points", post!!.value)

        val parameterAnnotations = method.parameterAnnotations
        assertHasAuthorizationCarAndTrip(parameterAnnotations)
        assertTrue(parameterAnnotations[3].any { it is Body })
    }

    @Test
    fun finishTripUsesTripFinishRoute() {
        val method = TripApi::class.java.declaredMethods.single {
            it.name == "finishTrip"
        }

        val post = method.getAnnotation(POST::class.java)
        assertNotNull(post)
        assertEquals("api/v1/cars/{car_id}/trips/{trip_id}/finish", post!!.value)

        val parameterAnnotations = method.parameterAnnotations
        assertHasAuthorizationCarAndTrip(parameterAnnotations)
        assertTrue(parameterAnnotations[3].any { it is Body })
    }

    @Test
    fun readMethodsUseTripReadRoutes() {
        val activeTrip = TripApi::class.java.declaredMethods.single {
            it.name == "activeTrip"
        }
        val trips = TripApi::class.java.declaredMethods.single {
            it.name == "trips"
        }
        val tripDetails = TripApi::class.java.declaredMethods.single {
            it.name == "tripDetails"
        }

        val activeTripGet = activeTrip.getAnnotation(GET::class.java)
        val tripsGet = trips.getAnnotation(GET::class.java)
        val tripDetailsGet = tripDetails.getAnnotation(GET::class.java)

        assertNotNull(activeTripGet)
        assertNotNull(tripsGet)
        assertNotNull(tripDetailsGet)
        assertEquals("api/v1/cars/{car_id}/trips/active", activeTripGet!!.value)
        assertEquals("api/v1/cars/{car_id}/trips", tripsGet!!.value)
        assertEquals("api/v1/cars/{car_id}/trips/{trip_id}", tripDetailsGet!!.value)
    }

    private fun assertHasAuthorizationCarAndBody(parameterAnnotations: Array<Array<Annotation>>) {
        assertTrue(
            parameterAnnotations[0].any {
                it is Header && it.value == "Authorization"
            }
        )
        assertTrue(
            parameterAnnotations[1].any {
                it is Path && it.value == "car_id"
            }
        )
        assertTrue(parameterAnnotations[2].any { it is Body })
    }

    private fun assertHasAuthorizationCarAndTrip(parameterAnnotations: Array<Array<Annotation>>) {
        assertTrue(
            parameterAnnotations[0].any {
                it is Header && it.value == "Authorization"
            }
        )
        assertTrue(
            parameterAnnotations[1].any {
                it is Path && it.value == "car_id"
            }
        )
        assertTrue(
            parameterAnnotations[2].any {
                it is Path && it.value == "trip_id"
            }
        )
    }
}
