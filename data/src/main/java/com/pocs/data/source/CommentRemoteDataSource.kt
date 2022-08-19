package com.pocs.data.source

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.comment.CommentAddBody
import com.pocs.data.model.comment.CommentsDto
import retrofit2.Response

interface CommentRemoteDataSource {
    suspend fun getAllBy(postId: Int): Response<ResponseBody<CommentsDto>>
    suspend fun add(commentAddBody: CommentAddBody): Response<ResponseBody<Unit>>
    suspend fun delete(commentId: Int): Response<ResponseBody<Unit>>
}