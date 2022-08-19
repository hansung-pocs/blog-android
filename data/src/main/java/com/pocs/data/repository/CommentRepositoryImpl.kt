package com.pocs.data.repository

import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toEntity
import com.pocs.data.model.comment.CommentAddBody
import com.pocs.data.source.CommentRemoteDataSource
import com.pocs.domain.model.comment.Comment
import com.pocs.domain.repository.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val dataSource: CommentRemoteDataSource
) : CommentRepository {

    override suspend fun getAllBy(postId: Int): Result<List<Comment>> {
        return try {
            val response = dataSource.getAllBy(postId = postId)
            if (response.isSuccessful) {
                val comments = response.body()!!.data.comments.map { it.toEntity() }
                Result.success(comments)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun add(content: String, postId: Int, parentId: Int?): Result<Unit> {
        return try {
            val requestBody = CommentAddBody(
                content = content,
                postId = postId,
                parentId = parentId
            )
            val response = dataSource.add(requestBody)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(commentId: Int, content: String): Result<Unit> {
        return try {
            val response = dataSource.update(commentId = commentId, content = content)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw  Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(commentId: Int): Result<Unit> {
        return try {
            val response = dataSource.delete(commentId = commentId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
