package com.pocs.test_library.fake.source.remote

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.UserCreateInfoBody
import com.pocs.data.model.admin.UserKickInfoBody
import com.pocs.data.model.user.UserDto
import com.pocs.data.source.AdminRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class FakeAdminRemoteDataSource @Inject constructor() : AdminRemoteDataSource {

    override suspend fun getUserDetail(userId: Int): Response<ResponseBody<UserDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(
        userCreateInfoBody: UserCreateInfoBody
    ): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun kickUser(
        id: Int,
        userKickInfoBody: UserKickInfoBody
    ): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }
}
