package com.pocs.data.repository

import com.pocs.data.extension.getDataOrThrowMessage
import com.pocs.data.mapper.toEntity
import com.pocs.data.model.comment.CommentAddBody
import com.pocs.data.model.comment.CommentUpdateBody
import com.pocs.data.source.CommentRemoteDataSource
import com.pocs.domain.model.comment.Comment
import com.pocs.domain.repository.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val dataSource: CommentRemoteDataSource
) : CommentRepository {

    override suspend fun getAllBy(postId: Int): Result<List<Comment>> {
        return runCatching {
            val response = dataSource.getAllBy(postId = postId)
            response.getDataOrThrowMessage().comments.map { it.toEntity() }
        }
    }

    override suspend fun add(content: String, postId: Int, parentId: Int?): Result<Unit> {
        return runCatching {
            val requestBody = CommentAddBody(
                content = content,
                postId = postId,
                parentId = parentId
            )
            val response = dataSource.add(requestBody)
            response.getDataOrThrowMessage()
        }
    }

    override suspend fun update(commentId: Int, content: String): Result<Unit> {
        return runCatching {
            val response = dataSource.update(
                commentId = commentId,
                commentUpdateBody = CommentUpdateBody(content = content)
            )
            response.getDataOrThrowMessage()
        }
    }

    override suspend fun delete(commentId: Int): Result<Unit> {
        return runCatching {
            val response = dataSource.delete(commentId = commentId)
            response.getDataOrThrowMessage()
        }
    }
}
