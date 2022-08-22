package com.pocs.test_library.fake

import com.pocs.domain.model.comment.Comment
import com.pocs.domain.model.comment.CommentWriter
import com.pocs.domain.repository.CommentRepository
import javax.inject.Inject

class FakeCommentRepositoryImpl @Inject constructor() : CommentRepository {

    var idCounter = 0
        private set

    private val comments = mutableListOf<Comment>()

    var isSuccessToGetAllBy = true
    var isSuccessToAdd = true
    var isSuccessToUpdate = true
    var isSuccessToDelete = true

    override suspend fun getAllBy(postId: Int): Result<List<Comment>> {
        return if (isSuccessToGetAllBy) Result.success(comments) else Result.failure(Exception("ee"))
    }

    override suspend fun add(content: String, postId: Int, parentId: Int?): Result<Unit> {
        if (!isSuccessToAdd) {
            return Result.failure(Exception("error"))
        }
        val parentIndex = comments.indexOfFirst { it.id == parentId }
        val newComment = Comment(
            id = idCounter++,
            parentId = parentId,
            childrenCount = 0,
            postId = postId,
            writer = CommentWriter(
                userId = 1,
                name = "홍길동"
            ),
            content = content,
            createdAt = "2022-09-02 13:09",
            updatedAt = null
        )
        var index = 0
        if (parentIndex != -1) {
            while (comments.size < index && comments[parentIndex + index].parentId == parentId) {
                ++index
            }
        }
        comments.add(index, newComment)
        return Result.success(Unit)
    }

    override suspend fun update(commentId: Int, content: String): Result<Unit> {
        if (!isSuccessToUpdate) {
            return Result.failure(Exception("error"))
        }
        val prevComment = comments.first { it.id == commentId }
        val newComment = prevComment.copy(content = content)
        val index = comments.indexOf(prevComment)
        comments[index] = newComment
        return Result.success(Unit)
    }

    override suspend fun delete(commentId: Int): Result<Unit> {
        if (!isSuccessToDelete) {
            return Result.failure(Exception("ee"))
        }
        comments.removeAll { it.id == commentId }
        return Result.success(Unit)
    }
}
