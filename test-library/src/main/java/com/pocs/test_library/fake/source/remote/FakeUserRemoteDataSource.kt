package com.pocs.test_library.fake.source.remote

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserDto
import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
import com.pocs.data.source.UserRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class FakeUserRemoteDataSource @Inject constructor() : UserRemoteDataSource {
    override suspend fun getUserDetail(id: Int): Response<ResponseBody<UserDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(
        id: Int,
        name: String,
        password: String,
        email: String,
        company: String?,
        github: String?
    ): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun createAnonymous(AnonymousCreateInfoBody: AnonymousCreateInfoBody): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }
}