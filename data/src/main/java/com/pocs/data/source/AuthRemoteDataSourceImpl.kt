package com.pocs.data.source

import com.pocs.data.api.AuthApi
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.auth.LoginRequestBody
import com.pocs.data.model.auth.SessionValidResponseData
import retrofit2.Response
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApi
) : AuthRemoteDataSource {

    override suspend fun login(loginRequestBody: LoginRequestBody) = api.login(loginRequestBody)

    override suspend fun logout(token: String) = api.logout(token)

    override suspend fun isSessionValid(
        token: String
    ): Response<ResponseBody<SessionValidResponseData>> = api.isSessionValid(token)
}