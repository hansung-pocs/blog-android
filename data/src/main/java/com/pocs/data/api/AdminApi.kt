package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.AdminUserListDto
import retrofit2.http.GET

interface AdminApi {
    @GET("admin/users")
    suspend fun getAllUsers(): ResponseBody<AdminUserListDto>
}