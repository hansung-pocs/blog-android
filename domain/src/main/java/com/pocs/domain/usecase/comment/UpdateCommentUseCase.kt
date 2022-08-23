package com.pocs.domain.usecase.comment

import com.pocs.domain.repository.CommentRepository
import javax.inject.Inject

class UpdateCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(commentId: Int, content: String) =
        repository.update(commentId = commentId, content = content)
}