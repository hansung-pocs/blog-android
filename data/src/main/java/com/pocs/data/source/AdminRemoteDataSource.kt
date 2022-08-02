package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.UserCreateInfoBody
import com.pocs.data.model.admin.AdminUserDto
import com.pocs.data.model.admin.UserKickInfoBody

interface AdminRemoteDataSource {
    suspend fun getUserDetail(userId: Int): ResponseBody<AdminUserDto>
    suspend fun createUser(userCreateInfoBody: UserCreateInfoBody): ResponseBody<Unit>
    suspend fun kickUser(id: Int, userKickInfoBody: UserKickInfoBody): ResponseBody<Unit>
}