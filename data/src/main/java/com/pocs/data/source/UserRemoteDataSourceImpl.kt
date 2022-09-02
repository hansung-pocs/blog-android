package com.pocs.data.source

import android.graphics.Bitmap
import com.pocs.data.api.UserApi
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserUpdateBody
import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
import retrofit2.Response
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val api: UserApi
) : UserRemoteDataSource {

    override suspend fun getUserDetail(id: Int) = api.getUserDetail(id = id)

    override suspend fun updateUser(
        id: Int,
        name: String,
        password: String?,
        email: String,
        company: String?,
        github: String?,
        profileImageUrl: Bitmap?
    ) = api.updateUser(
        userId = id,
        userUpdateBody = UserUpdateBody(
            password = password,
            name = name,
            email = email,
            github = github,
            company = company,
            profileImageUrl = profileImageUrl
        )
    )

    override suspend fun createAnonymous(
        AnonymousCreateInfoBody: AnonymousCreateInfoBody
    ): Response<ResponseBody<Unit>> {
        return api.createAnonymous(AnonymousCreateInfoBody)
    }
}