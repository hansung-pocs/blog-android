package com.pocs.domain.repository

import com.pocs.domain.model.comment.Comment

interface CommentRepository {
    suspend fun getAllBy(postId: Int): Result<List<Comment>>
    suspend fun add(content: String, postId: Int, parentId: Int?): Result<Unit>
    suspend fun update(commentId: Int, content: String): Result<Unit>
    suspend fun delete(commentId: Int): Result<Unit>
}