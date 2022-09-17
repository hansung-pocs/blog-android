package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostFilterType
import com.pocs.domain.repository.PostRepository
import javax.inject.Inject

class GetAllPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(filterType: PostFilterType) = repository.getAll(filterType)
}
