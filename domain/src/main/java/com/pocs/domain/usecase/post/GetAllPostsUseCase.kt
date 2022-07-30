package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.repository.PostRepository
import javax.inject.Inject

class GetAllPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(category: PostCategory) = repository.getAll(category)
}