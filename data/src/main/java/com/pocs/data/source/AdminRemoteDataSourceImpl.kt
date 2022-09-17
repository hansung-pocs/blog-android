package com.pocs.data.source

import com.pocs.data.api.AdminApi
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.UserCreateInfoBody
import com.pocs.data.model.admin.UserKickInfoBody
import retrofit2.Response
import javax.inject.Inject

class AdminRemoteDataSourceImpl @Inject constructor(
    private val api: AdminApi
) : AdminRemoteDataSource {

    override suspend fun getUserDetail(userId: Int) = api.getUserDetail(userId)

    override suspend fun createUser(
        userCreateInfoBody: UserCreateInfoBody
    ): Response<ResponseBody<Unit>> {
        return api.createUser(userCreateInfoBody)
    }

    override suspend fun kickUser(
        id: Int,
        userKickInfoBody: UserKickInfoBody
    ): Response<ResponseBody<Unit>> {
        return api.kickUser(id, userKickInfoBody)
    }
}
