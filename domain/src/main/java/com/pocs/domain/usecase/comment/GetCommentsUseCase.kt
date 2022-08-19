package com.pocs.domain.usecase.comment

import com.pocs.domain.repository.CommentRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: CommentRepository
) {
    suspend operator fun invoke(postId: Int) = repository.getAllBy(postId = postId)
}