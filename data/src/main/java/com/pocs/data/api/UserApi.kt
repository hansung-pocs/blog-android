package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
import com.pocs.data.model.user.UserDto
import com.pocs.data.model.user.UserListDto
import com.pocs.data.model.user.UserListSortingMethodDto
import okhttp3.MultipartBody
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

    @Multipart
    @PATCH("users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Part password: MultipartBody.Part?,
        @Part name: MultipartBody.Part,
        @Part email: MultipartBody.Part,
        @Part github: MultipartBody.Part?,
        @Part company: MultipartBody.Part?,
        @Part profileImage: MultipartBody.Part?
    ): Response<ResponseBody<Unit>>

    @POST("users")
    suspend fun createAnonymous(
        @Body anonymousCreateInfoBody: AnonymousCreateInfoBody
    ): Response<ResponseBody<Unit>>
}