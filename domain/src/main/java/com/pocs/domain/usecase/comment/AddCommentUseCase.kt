package com.pocs.domain.usecase.comment

import com.pocs.domain.repository.CommentRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(content: String, postId: Int, parentId: Int?) =
        repository.add(content = content, postId = postId, parentId = parentId)
}
