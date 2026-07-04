package com.lamba.app.data.statistics

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

class StatisticsApiTest {
    @Test
    fun statisticsEndpointUsesCarStatisticsRoute() {
        val method = StatisticsApi::class.java.declaredMethods.single {
            it.name == "statistics"
        }

        val get = method.getAnnotation(GET::class.java)
        assertEquals("api/v1/cars/{car_id}/statistics", get.value)

        val parameterAnnotations = method.parameterAnnotations
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
    }

    @Test
    fun statisticsResponseMapsHalfYearField() {
        val response = Gson().fromJson(
            """
            {
              "month": [],
              "half_year": [
                {
                  "period_key": "2026-h1",
                  "period_title": "Jan-Jun 2026",
                  "metrics": [],
                  "dynamics": [],
                  "dynamics_style": "bar",
                  "categories": [],
                  "total_amount": "0.00",
                  "total_label": "6 months"
                }
              ],
              "year": []
            }
            """.trimIndent(),
            CarStatisticsResponse::class.java
        )

        assertEquals("Jan-Jun 2026", response.halfYear.single().periodTitle)
        assertEquals("6 months", response.halfYear.single().totalLabel)
    }
}
