package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.auth.LoginRequestBody
import com.pocs.data.model.auth.LoginResponseData
import com.pocs.data.model.auth.SessionValidResponseData
import retrofit2.Response

interface AuthRemoteDataSource {
    suspend fun login(loginRequestBody: LoginRequestBody): Response<ResponseBody<LoginResponseData>>
    suspend fun logout(): Response<ResponseBody<Unit>>
    suspend fun isSessionValid(): Response<ResponseBody<SessionValidResponseData>>
}