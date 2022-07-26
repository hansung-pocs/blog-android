package com.pocs.data.source

import com.pocs.data.model.PostListDto
import com.pocs.data.model.ResponseBody

interface PostRemoteDataSource {
    suspend fun getAll(): ResponseBody<PostListDto>
}