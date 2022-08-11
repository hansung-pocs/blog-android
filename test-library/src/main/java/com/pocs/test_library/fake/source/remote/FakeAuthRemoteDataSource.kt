package com.pocs.test_library.fake.source.remote

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.auth.LoginRequestBody
import com.pocs.data.model.auth.LoginResponseData
import com.pocs.data.source.AuthRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class FakeAuthRemoteDataSource @Inject constructor() : AuthRemoteDataSource {

    var loginResponse: Response<ResponseBody<LoginResponseData>> = Response.success(
        ResponseBody(
            message = "",
            status = 200,
            serverTime = "",
            data = LoginResponseData(sessionToken = "abc", userId = 1)
        )
    )

    var logoutResponse: Response<ResponseBody<Unit>> = Response.success(
        ResponseBody(
            message = "",
            status = 200,
            serverTime = "",
            data = Unit
        )
    )

    var isSessionValidResponse: Response<ResponseBody<Unit>> = Response.success(
        ResponseBody(
            message = "",
            status = 200,
            serverTime = "",
            data = Unit
        )
    )

    var isSessionValidInnerLambda: () -> Unit = {}

    override suspend fun login(loginRequestBody: LoginRequestBody): Response<ResponseBody<LoginResponseData>> {
        return loginResponse
    }

    override suspend fun logout(token: String): Response<ResponseBody<Unit>> {
        return logoutResponse
    }

    override suspend fun isSessionValid(token: String): Response<ResponseBody<Unit>> {
        isSessionValidInnerLambda()
        return isSessionValidResponse
    }
}