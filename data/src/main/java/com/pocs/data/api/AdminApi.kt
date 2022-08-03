package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.UserCreateInfoBody
import com.pocs.data.model.admin.AdminUserDto
import com.pocs.data.model.admin.AdminUserListDto
import com.pocs.data.model.admin.UserKickInfoBody
import com.pocs.data.model.post.PostListDto
import retrofit2.http.*

interface AdminApi {
    @GET("admin/users")
    suspend fun getAllUsers(): ResponseBody<AdminUserListDto>

    @GET("admin/users/{userId}")
    suspend fun getUserDetail(
        @Path("userId") userId: Int
    ): ResponseBody<AdminUserDto>

    @POST("admin/users")
    suspend fun createUser(
        @Body userCreateInfoBody: UserCreateInfoBody
    ): ResponseBody<Unit>

    @PATCH("admin/users/{userId}/kick")
    suspend fun kickUser(
        @Path("userId") userId: Int,
        @Body userKickInfoBody: UserKickInfoBody
    ): ResponseBody<Unit>

    @GET("admin/posts")
    suspend fun getAllPosts(): ResponseBody<PostListDto>

    @GET("admin/posts/{userId}")
    suspend fun getAllPostsByUser(
        @Path("userId") userId: Int
    ): ResponseBody<PostListDto>
}