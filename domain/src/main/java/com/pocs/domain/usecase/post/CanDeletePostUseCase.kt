package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostDetail
import com.pocs.domain.model.user.UserType
import com.pocs.domain.repository.AuthRepository
import javax.inject.Inject

class CanDeletePostUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(postDetail: PostDetail): Boolean {
        val currentUser = repository.getCurrentUser().value ?: return false

        if (currentUser.type == UserType.ADMIN) {
            return true
        }
        return currentUser.id == postDetail.writer.id
    }
}