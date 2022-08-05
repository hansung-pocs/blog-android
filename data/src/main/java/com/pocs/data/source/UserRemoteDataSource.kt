package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserDto
import retrofit2.Response

interface UserRemoteDataSource {

    suspend fun getUserDetail(id: Int): Response<ResponseBody<UserDto>>

    suspend fun updateUser(
        id: Int,
        name: String,
        password: String,
        email: String,
        company: String?,
        github: String?
    ): Response<ResponseBody<Unit>>
}