package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.repository.PostRepository
import javax.inject.Inject

class AddPostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        userId: Int,
        category: PostCategory
    ) = repository.addPost(title = title, content = content, userId = userId, category = category)
}