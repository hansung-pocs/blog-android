package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.*
import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
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

    @PATCH("users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Body userUpdateBody: UserUpdateBody
    ): Response<ResponseBody<Unit>>

    @Multipart
    @PATCH("users/{userId}/profile")
    suspend fun uploadProfileImage(
        @Path("userId") userId: Int,
        @Part image: MultipartBody.Part?
    ): Response<ResponseBody<UserProfileUpdateResponse>>

    @POST("users")
    suspend fun createAnonymous(
        @Body anonymousCreateInfoBody: AnonymousCreateInfoBody
    ): Response<ResponseBody<Unit>>
}
