package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.auth.LoginRequestBody
import com.pocs.data.model.auth.LoginResponseData
import com.pocs.data.model.auth.SessionValidResponseData
import com.pocs.data.model.user.UserDto
import retrofit2.Response

interface AuthRemoteDataSource {
    suspend fun login(loginRequestBody: LoginRequestBody): Response<ResponseBody<LoginResponseData>>
    suspend fun logout(token: String): Response<ResponseBody<Unit>>
    suspend fun isSessionValid(token: String): Response<ResponseBody<SessionValidResponseData>>
}