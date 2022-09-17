package com.pocs.domain.usecase.post

import com.pocs.domain.repository.AuthRepository
import com.pocs.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit> {
        val currentUserId = authRepository.getCurrentUser().value?.id
            ?: return Result.failure(Exception("삭제할 권한이 없습니다."))

        return postRepository.deletePost(postId = postId, userId = currentUserId)
    }
}
