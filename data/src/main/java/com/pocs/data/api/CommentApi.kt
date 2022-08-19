package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.comment.CommentAddBody
import com.pocs.data.model.comment.CommentsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApi {
    @GET("comments/{postId}")
    suspend fun getAllBy(
        @Path("postId") postId: Int
    ): Response<ResponseBody<CommentsDto>>

    @POST("comments")
    suspend fun add(
        @Body commentAddBody: CommentAddBody
    ): Response<ResponseBody<Unit>>
}