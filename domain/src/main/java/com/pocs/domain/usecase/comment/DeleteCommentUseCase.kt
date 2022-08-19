package com.pocs.domain.usecase.comment

import com.pocs.domain.repository.CommentRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(commentId: Int) = repository.delete(commentId = commentId)
}