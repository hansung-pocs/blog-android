package com.pocs.data.source

import com.pocs.data.api.AuthApi
import com.pocs.data.model.auth.LoginRequestBody
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApi
) : AuthRemoteDataSource {

    override suspend fun login(loginRequestBody: LoginRequestBody) = api.login(loginRequestBody)

    override suspend fun logout() = api.logout()

    override suspend fun isSessionValid() = api.isSessionValid()
}