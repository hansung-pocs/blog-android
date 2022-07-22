package com.pocs.domain.usecase.post

import com.pocs.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        postId: Int,
        userId: Int
    ) = repository.deletePost(postId = postId, userId = userId)
}