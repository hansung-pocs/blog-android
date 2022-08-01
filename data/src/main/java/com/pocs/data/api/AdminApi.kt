package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.AdminUserDto
import com.pocs.data.model.admin.AdminUserListDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AdminApi {
    @GET("admin/users")
    suspend fun getAllUsers(): ResponseBody<AdminUserListDto>

    @GET("admin/users/{userId}")
    suspend fun getUserDetail(
        @Path("userId") userId: Int
    ): ResponseBody<AdminUserDto>
}