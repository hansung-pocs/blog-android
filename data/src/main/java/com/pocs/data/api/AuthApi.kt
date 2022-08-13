package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.auth.LoginRequestBody
import com.pocs.data.model.auth.LoginResponseData
import com.pocs.data.model.user.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    @Headers("x-pocs-device-type: android")
    suspend fun login(
        @Body loginRequestBody: LoginRequestBody
    ): Response<ResponseBody<LoginResponseData>>

    @POST("auth/logout")
    suspend fun logout(
        @Header("x-pocs-session-token") token: String
    ): Response<ResponseBody<Unit>>

    @POST("auth/validation")
    suspend fun isSessionValid(
        @Header("x-pocs-session-token") token: String
    ): Response<ResponseBody<UserDto>>
}