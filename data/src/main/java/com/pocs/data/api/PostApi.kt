package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.post.*
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
        @Body postAddBody: PostAddBody,
    ): ResponseBody<Unit>

    @PATCH("posts/{postId}/delete")
    suspend fun deletePost(
        @Path("postId") postId: Int,
        @Body postDeleteBody: PostDeleteBody
    ): ResponseBody<Unit>

    @PATCH("posts/{postId}")
    suspend fun updatePost(
        @Path("postId") PostId: Int,
        @Body postUpdateBody: PostUpdateBody
    ): ResponseBody<Unit>
}