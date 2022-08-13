package com.pocs.test_library.fake.source.remote

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.auth.LoginRequestBody
import com.pocs.data.model.auth.LoginResponseData
import com.pocs.data.model.user.UserDto
import com.pocs.data.source.AuthRemoteDataSource
import com.pocs.test_library.mock.errorResponse
import retrofit2.Response
import javax.inject.Inject

class FakeAuthRemoteDataSource @Inject constructor() : AuthRemoteDataSource {

    var loginResponse: Response<ResponseBody<LoginResponseData>> = errorResponse()

    var logoutResponse: Response<ResponseBody<Unit>> = errorResponse()

    var isSessionValidResponse: Response<ResponseBody<UserDto>> = errorResponse()

    var isSessionValidInnerLambda: () -> Unit = {}

    override suspend fun login(loginRequestBody: LoginRequestBody): Response<ResponseBody<LoginResponseData>> {
        return loginResponse
    }

    override suspend fun logout(token: String): Response<ResponseBody<Unit>> {
        return logoutResponse
    }

    override suspend fun isSessionValid(token: String): Response<ResponseBody<UserDto>> {
        isSessionValidInnerLambda()
        return isSessionValidResponse
    }
}