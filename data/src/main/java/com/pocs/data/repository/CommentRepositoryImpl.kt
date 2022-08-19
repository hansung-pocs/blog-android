package com.pocs.data.repository

import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toEntity
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
}