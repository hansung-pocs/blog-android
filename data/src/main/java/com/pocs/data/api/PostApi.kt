package com.pocs.data.api

import com.pocs.data.model.PostCreateDto
import com.pocs.data.model.PostDetailDto
import com.pocs.data.model.PostListDto
import com.pocs.data.model.ResponseBody
import retrofit2.http.*

interface PostApi {
    @GET("posts")
    suspend fun getAll(): ResponseBody<PostListDto>

    @GET("posts/{postId}")
    suspend fun getPostDetail(
        @Path("postId") postId: Int
    ): ResponseBody<PostDetailDto>

    @POST("posts")
    suspend fun addPost(
        @Body postCreateDto: PostCreateDto,
    ): ResponseBody<Unit>
}