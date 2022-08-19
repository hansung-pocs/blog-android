package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.post.*
import retrofit2.Response
import retrofit2.http.*

interface PostApi {
    @GET("posts")
    suspend fun getAll(
        @Query("id") id: String?
    ): Response<ResponseBody<PostListDto>>

    @GET("posts/{postId}")
    suspend fun getPostDetail(
        @Path("postId") postId: Int
    ): Response<ResponseBody<PostDetailDto>>

    @POST("posts")
    suspend fun addPost(
        @Body postAddBody: PostAddBody,
    ): Response<ResponseBody<Unit>>

    @PATCH("posts/{postId}/delete")
    suspend fun deletePost(
        @Path("postId") postId: Int,
        @Body postDeleteBody: PostDeleteBody
    ): Response<ResponseBody<Unit>>

    @PATCH("posts/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: Int,
        @Body postUpdateBody: PostUpdateBody
    ): Response<ResponseBody<Unit>>
}