package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.repository.PostRepository
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import javax.inject.Inject

class AddPostUseCase @Inject constructor(
    private val repository: PostRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        onlyMember: Boolean,
        category: PostCategory
    ): Result<Unit> {
        return repository.addPost(
            title = title,
            onlyMember = onlyMember,
            content = content,
            userId = requireNotNull(getCurrentUserUseCase()?.id),
            category = category
        )
    }
}
