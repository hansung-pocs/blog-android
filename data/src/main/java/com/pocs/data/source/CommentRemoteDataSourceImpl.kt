package com.pocs.data.source

import com.pocs.data.api.CommentApi
import com.pocs.data.model.comment.CommentAddBody
import javax.inject.Inject

class CommentRemoteDataSourceImpl @Inject constructor(
    private val api: CommentApi
) : CommentRemoteDataSource {
    override suspend fun getAllBy(postId: Int) = api.getAllBy(postId = postId)
    override suspend fun add(commentAddBody: CommentAddBody) = api.add(commentAddBody)
    override suspend fun delete(commentId: Int) = api.delete(commentId = commentId)
}