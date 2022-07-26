package com.pocs.data.api

import com.pocs.data.model.PostDetailDto
import com.pocs.data.model.PostListDto
import com.pocs.data.model.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface PostApi {
    @GET("posts")
    suspend fun getAll(): ResponseBody<PostListDto>

    @GET("posts/{postId}")
    suspend fun getDetail(
        @Path("postId") postId: Int
    ): ResponseBody<PostDetailDto>
}