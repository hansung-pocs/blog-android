package com.pocs.data.source

import com.pocs.data.api.UserApi
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val api: UserApi
) : UserRemoteDataSource {

    override suspend fun getUserDetail(id: Int) = api.getUserDetail(id = id)
}