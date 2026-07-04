package com.lamba.app.data.records

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

class RecordsApiTest {
    @Test
    fun uploadRecordPhotosUsesRecordPhotoRoute() {
        val method = RecordsApi::class.java.declaredMethods.single {
            it.name == "uploadRecordPhotos"
        }

        assertNotNull(method.getAnnotation(Multipart::class.java))

        val post = method.getAnnotation(POST::class.java)
        assertEquals("api/v1/cars/{car_id}/records/{record_id}/photos", post.value)

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
        assertTrue(
            parameterAnnotations[2].any {
                it is Path && it.value == "record_id"
            }
        )
        assertTrue(
            parameterAnnotations[3].any {
                it is Part
            }
        )
    }
}
