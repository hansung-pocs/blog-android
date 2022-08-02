package com.pocs.domain.usecase.post

import com.pocs.domain.model.post.PostDetail
import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class CanEditPostUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(postDetail: PostDetail): Boolean {
        val currentUser = repository.getCurrentUserDetail()
        return currentUser.id == postDetail.writer.id
    }
}