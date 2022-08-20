package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.post.*
import retrofit2.Response

interface PostRemoteDataSource {
    suspend fun getPostDetail(postId: Int): Response<ResponseBody<PostDetailDto>>
    suspend fun addPost(postAddBody: PostAddBody): Response<ResponseBody<Unit>>
    suspend fun deletePost(postId: Int, postDeleteBody: PostDeleteBody): Response<ResponseBody<Unit>>
    suspend fun updatePost(postId: Int, postUpdateBody: PostUpdateBody): Response<ResponseBody<Unit>>
}