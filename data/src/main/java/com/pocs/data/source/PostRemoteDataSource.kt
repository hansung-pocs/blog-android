package com.pocs.data.source

import com.pocs.data.model.post.PostAddBody
import com.pocs.data.model.post.PostDetailDto
import com.pocs.data.model.post.PostListDto
import com.pocs.data.model.ResponseBody

interface PostRemoteDataSource {
    suspend fun getAll(): ResponseBody<PostListDto>
    suspend fun getPostDetail(postId: Int): ResponseBody<PostDetailDto>
    suspend fun addPost(postAddBody: PostAddBody): ResponseBody<Unit>
}