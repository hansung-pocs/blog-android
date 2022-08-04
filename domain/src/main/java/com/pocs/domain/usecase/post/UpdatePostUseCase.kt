package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.repository.PostRepository
import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val repository: PostRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        id: Int,
        title: String,
        content: String,
        category: PostCategory
    ) = repository.updatePost(
        id = id,
        title = title,
        content = content,
        userId = userRepository.getCurrentUserDetail().id,
        category = category
    )
}