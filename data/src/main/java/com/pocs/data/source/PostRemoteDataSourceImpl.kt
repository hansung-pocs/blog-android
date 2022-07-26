package com.pocs.data.source

import com.pocs.data.api.PostApi
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor(
    private val api: PostApi
): PostRemoteDataSource {

    override suspend fun getAll() = api.getAll()
}