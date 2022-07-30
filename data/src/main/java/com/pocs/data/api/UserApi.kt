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
        @Query("sort") sort: UserListSortingMethodDto
    ): ResponseBody<UserListDto>

    @GET("users/{userId}")
    suspend fun getUserDetail(
        @Path("userId") id: Int
    ): ResponseBody<UserDto>

    /**
     * 유저 정보를 업데이트한다.
     *
     * 성공시 302 리다이렉션 코드가 전달되기 때문에 반환 값을 [ResponseBody]이 아닌 [Response]로 해야한다.
     */
    @PATCH("users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Body userUpdateBody: UserUpdateBody
    ): Response<Unit>
}