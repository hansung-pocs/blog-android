package com.pocs.data.source

import com.pocs.data.api.AdminApi
import javax.inject.Inject

class AdminRemoteDataSourceImpl @Inject constructor(
    private val api: AdminApi
): AdminRemoteDataSource {

    override suspend fun getUserDetail(userId: Int) = api.getUserDetail(userId)
}