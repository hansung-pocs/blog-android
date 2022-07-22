package com.pocs.domain.usecase.post

import com.pocs.domain.model.PostCategory
import com.pocs.domain.repository.PostRepository
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        userId: Int,
        category: PostCategory
    ) = repository.updatePost(title = title, content = content, userId = userId, category = category)
}