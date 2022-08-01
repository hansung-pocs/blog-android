package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.AdminUserDto

interface AdminRemoteDataSource {
    suspend fun getUserDetail(userId: Int): ResponseBody<AdminUserDto>
}