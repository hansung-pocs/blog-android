package com.pocs.data.api

import com.pocs.data.model.post.PostAddBody
import com.pocs.data.model.post.PostDetailDto
import com.pocs.data.model.post.PostListDto
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
        @Body postAddBody: PostAddBody,
    ): ResponseBody<Unit>
}