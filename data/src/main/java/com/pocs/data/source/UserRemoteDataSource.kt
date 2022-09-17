package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserDto
import com.pocs.data.model.user.UserProfileUpdateResponse
import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
import retrofit2.Response
import java.io.File

interface UserRemoteDataSource {

    suspend fun getUserDetail(id: Int): Response<ResponseBody<UserDto>>

    suspend fun updateUser(
        id: Int,
        name: String,
        password: String?,
        email: String,
        company: String?,
        github: String?
    ): Response<ResponseBody<Unit>>

    suspend fun uploadProfileImage(
        id: Int,
        profileImage: File?
    ): Response<ResponseBody<UserProfileUpdateResponse>>

    suspend fun createAnonymous(
        AnonymousCreateInfoBody: AnonymousCreateInfoBody
    ): Response<ResponseBody<Unit>>
}
