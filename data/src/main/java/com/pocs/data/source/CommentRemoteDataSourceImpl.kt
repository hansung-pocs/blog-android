package com.pocs.data.source

import com.pocs.data.api.CommentApi
import javax.inject.Inject

class CommentRemoteDataSourceImpl @Inject constructor(
    private val api: CommentApi
) : CommentRemoteDataSource {
    override suspend fun getAllBy(postId: Int) = api.getAllBy(postId = postId)
}