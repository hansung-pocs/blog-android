package com.pocs.test_library.fake.source.remote

import com.pocs.data.model.ResponseBody
import com.pocs.data.model.comment.CommentAddBody
import com.pocs.data.model.comment.CommentsDto
import com.pocs.data.source.CommentRemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class FakeCommentRemoteDataSource @Inject constructor() : CommentRemoteDataSource {
    override suspend fun getAllBy(postId: Int): Response<ResponseBody<CommentsDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun add(commentAddBody: CommentAddBody): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun update(commentId: Int, content: String): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(commentId: Int): Response<ResponseBody<Unit>> {
        TODO("Not yet implemented")
    }
}