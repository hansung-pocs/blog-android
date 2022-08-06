package com.pocs.data.source

import com.pocs.data.api.UserApi
import com.pocs.data.model.user.UserUpdateBody
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val api: UserApi
) : UserRemoteDataSource {

    override suspend fun getUserDetail(id: Int) = api.getUserDetail(id = id)

    override suspend fun updateUser(
        id: Int,
        name: String,
        password: String,
        email: String,
        company: String?,
        github: String?
    ) = api.updateUser(
        userId = id,
        userUpdateBody = UserUpdateBody(
            password = password,
            userName = name,
            email = email,
            github = github,
            company = company
        )
    )
}