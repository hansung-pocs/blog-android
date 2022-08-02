package com.pocs.domain.usecase.post

import com.pocs.domain.repository.PostRepository
import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(postId: Int) : Result<Unit> {
        val currentUserId = userRepository.getCurrentUserDetail().id
        return postRepository.deletePost(postId = postId, userId = currentUserId)
    }
}