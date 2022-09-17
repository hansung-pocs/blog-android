package com.pocs.test_library.mock

import com.pocs.data.model.ResponseBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

fun <T : Any> successResponse(data: T): Response<ResponseBody<T>> {
    return Response.success(
        ResponseBody(
            message = "",
            status = 200,
            serverTime = "",
            data = data
        )
    )
}

fun <T : Any> errorResponse(): Response<ResponseBody<T>> {
    return Response.error(
        403,
        "".toResponseBody("application/json".toMediaTypeOrNull())
    )
}
