package com.pocs.domain.usecase.post

import com.pocs.domain.repository.PostRepository
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Int) = repository.getPostDetail(id)
}
