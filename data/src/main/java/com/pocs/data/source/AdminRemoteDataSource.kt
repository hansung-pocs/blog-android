package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.UserCreateInfoBody
import com.pocs.data.model.admin.UserKickInfoBody
import com.pocs.data.model.user.UserDto
import retrofit2.Response

interface AdminRemoteDataSource {
    suspend fun getUserDetail(userId: Int): Response<ResponseBody<UserDto>>
    suspend fun createUser(userCreateInfoBody: UserCreateInfoBody): Response<ResponseBody<Unit>>
    suspend fun kickUser(id: Int, userKickInfoBody: UserKickInfoBody): Response<ResponseBody<Unit>>
}
