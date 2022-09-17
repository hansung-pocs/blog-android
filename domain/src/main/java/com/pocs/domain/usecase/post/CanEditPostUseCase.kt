package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostDetail
import com.pocs.domain.repository.AuthRepository
import javax.inject.Inject

class CanEditPostUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(postDetail: PostDetail): Boolean {
        val currentUser = repository.getCurrentUser().value
        return currentUser?.id == postDetail.writer.id
    }
}
