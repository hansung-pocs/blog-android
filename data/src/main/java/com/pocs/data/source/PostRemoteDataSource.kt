package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.post.*

interface PostRemoteDataSource {
    suspend fun getAll(): ResponseBody<PostListDto>
    suspend fun getPostDetail(postId: Int): ResponseBody<PostDetailDto>
    suspend fun addPost(postAddBody: PostAddBody): ResponseBody<Unit>
    suspend fun deletePost(postId: Int, postDeleteBody: PostDeleteBody): ResponseBody<Unit>
    suspend fun updatePost(postId: Int, postUpdateBody: PostUpdateBody): ResponseBody<Unit>
}