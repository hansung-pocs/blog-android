package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostDetail
import com.pocs.domain.model.user.UserType
import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class CanDeletePostUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(postDetail: PostDetail): Boolean {
        val currentUser = repository.getCurrentUserDetail()
        if (currentUser.type == UserType.ADMIN) {
            return true
        }
        return currentUser.id == postDetail.writer.id
    }
}