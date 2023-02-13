package com.pocs.data

import com.google.gson.Gson
import com.pocs.data.extension.getDataOrThrowMessage
import com.pocs.data.model.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import retrofit2.Response

class ResponseExtTest {

    @Test
    fun `should have data when calling getDataOrThrowMessage from successful response`() {
        // given
        val response =
            Response.success(ResponseBody(data = 10, message = "", status = 200, serverTime = ""))

        // when
        val data = response.getDataOrThrowMessage()

        // then
        assertEquals(10, data)
    }

    @Test
    fun `should throw error message when calling getDataOrThrowMessage from failure response`() {
        // given
        val message = "error message"
        val response = Response.error<ResponseBody<Int>>(
            404,
            Gson().toJson(
                ResponseBody(
                    data = null,
                    message = message,
                    status = 404,
                    serverTime = ""
                )
            ).toResponseBody(null)
        )

        // when, then
        assertThrows(message, Exception::class.java) { response.getDataOrThrowMessage() }
    }
}
