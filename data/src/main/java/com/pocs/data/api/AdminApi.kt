package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.UserCreateInfoBody
import com.pocs.data.model.admin.UserKickInfoBody
import com.pocs.data.model.post.PostListDto
import com.pocs.data.model.user.UserDto
import com.pocs.data.model.user.UserListDto
import retrofit2.Response
import retrofit2.http.*

interface AdminApi {
    @GET("admin/users")
    suspend fun getAllUsers(): Response<ResponseBody<UserListDto>>

    @GET("admin/users/{userId}")
    suspend fun getUserDetail(
        @Path("userId") userId: Int
    ): Response<ResponseBody<UserDto>>

    @POST("admin/users")
    suspend fun createUser(
        @Body userCreateInfoBody: UserCreateInfoBody
    ): Response<ResponseBody<Unit>>

    @PATCH("admin/users/{userId}/kick")
    suspend fun kickUser(
        @Path("userId") userId: Int,
        @Body userKickInfoBody: UserKickInfoBody
    ): Response<ResponseBody<Unit>>

    @GET("admin/posts")
    suspend fun getAllPosts(): Response<ResponseBody<PostListDto>>

    @GET("admin/posts/{userId}")
    suspend fun getAllPostsByUser(
        @Path("userId") userId: Int
    ): Response<ResponseBody<PostListDto>>
}