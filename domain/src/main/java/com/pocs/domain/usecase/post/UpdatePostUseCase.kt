package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.repository.AuthRepository
import com.pocs.domain.repository.PostRepository
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        id: Int,
        title: String,
        content: String,
        category: PostCategory
    ): Result<Unit> {
        val userId = authRepository.getCurrentUser().value?.id
            ?: return Result.failure(Exception("수정할 권한이 없습니다."))

        return postRepository.updatePost(
            postId = id,
            title = title,
            content = content,
            userId = userId,
            category = category
        )
    }
}