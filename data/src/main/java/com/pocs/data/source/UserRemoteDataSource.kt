package com.pocs.data.source

import android.graphics.Bitmap
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserDto
import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
import retrofit2.Response

interface UserRemoteDataSource {

    suspend fun getUserDetail(id: Int): Response<ResponseBody<UserDto>>

    suspend fun updateUser(
        id: Int,
        name: String,
        password: String?,
        email: String,
        company: String?,
        github: String?,
        profileImageUrl: Bitmap?
    ): Response<ResponseBody<Unit>>

    suspend fun createAnonymous(AnonymousCreateInfoBody: AnonymousCreateInfoBody): Response<ResponseBody<Unit>>
}