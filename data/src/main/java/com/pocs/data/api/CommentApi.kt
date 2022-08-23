package com.pocs.data.api

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.comment.CommentAddBody
import com.pocs.data.model.comment.CommentUpdateBody
import com.pocs.data.model.comment.CommentsDto
import retrofit2.Response
import retrofit2.http.*

interface CommentApi {
    @GET("comments/{postId}")
    suspend fun getAllBy(
        @Path("postId") postId: Int
    ): Response<ResponseBody<CommentsDto>>

    @POST("comments")
    suspend fun add(
        @Body commentAddBody: CommentAddBody
    ): Response<ResponseBody<Unit>>

    @PATCH("comments/{commentId}")
    suspend fun update(
        @Path("commentId") commentId: Int,
        @Body commentUpdateBody: CommentUpdateBody
    ): Response<ResponseBody<Unit>>

    @PATCH("comments/{commentId}/delete")
    suspend fun delete(
        @Path("commentId") commentId: Int
    ): Response<ResponseBody<Unit>>
}