package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.post.*
import retrofit2.Response

interface PostRemoteDataSource {
    suspend fun getAll(): ResponseBody<PostListDto>
    suspend fun getPostDetail(postId: Int): ResponseBody<PostDetailDto>
    suspend fun addPost(postAddBody: PostAddBody): ResponseBody<Unit>
    suspend fun deletePost(postId: Int, postDeleteBody: PostDeleteBody): ResponseBody<Unit>
    suspend fun updatePost(postId: Int, postUpdateBody: PostUpdateBody): Response<Unit>
}