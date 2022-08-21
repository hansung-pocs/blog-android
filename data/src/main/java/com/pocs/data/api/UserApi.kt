package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserDto
import com.pocs.data.model.user.UserListDto
import com.pocs.data.model.user.UserListSortingMethodDto
import com.pocs.data.model.user.UserUpdateBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("users")
    suspend fun getAll(
        @Query("sort") sortingMethod: UserListSortingMethodDto?,
        @Query("search") query: String?,
        @Query("offset") pageSize: Int,
        @Query("pageNum") page: Int
    ): Response<ResponseBody<UserListDto>>

    @GET("users/{userId}")
    suspend fun getUserDetail(
        @Path("userId") id: Int
    ): Response<ResponseBody<UserDto>>

    @PATCH("users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Body userUpdateBody: UserUpdateBody
    ): Response<ResponseBody<Unit>>
}