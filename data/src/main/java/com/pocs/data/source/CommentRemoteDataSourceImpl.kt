package com.pocs.data.source

import com.pocs.data.api.CommentApi
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.comment.CommentAddBody
import retrofit2.Response
import javax.inject.Inject

class CommentRemoteDataSourceImpl @Inject constructor(
    private val api: CommentApi
) : CommentRemoteDataSource {
    override suspend fun getAllBy(postId: Int) = api.getAllBy(postId = postId)
    override suspend fun add(commentAddBody: CommentAddBody) = api.add(commentAddBody)
}