package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.auth.LoginRequestBody
import com.pocs.data.model.auth.LoginResponseData
import com.pocs.data.model.auth.SessionValidResponseData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    @Headers("x-pocs-device-type: android")
    suspend fun login(
        @Body loginRequestBody: LoginRequestBody
    ): Response<ResponseBody<LoginResponseData>>

    @POST("auth/logout")
    suspend fun logout(): Response<ResponseBody<Unit>>

    @POST("auth/validation")
    suspend fun isSessionValid(): Response<ResponseBody<SessionValidResponseData>>
}