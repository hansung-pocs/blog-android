package com.pocs.data.source

import com.pocs.data.api.AdminApi
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.admin.UserCreateInfoBody
import javax.inject.Inject

class AdminRemoteDataSourceImpl @Inject constructor(
    private val api: AdminApi
): AdminRemoteDataSource {

    override suspend fun getUserDetail(userId: Int) = api.getUserDetail(userId)

    override suspend fun createUser(userCreateInfoBody: UserCreateInfoBody): ResponseBody<Unit> {
        return api.createUser(userCreateInfoBody)
    }
}