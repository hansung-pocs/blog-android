package com.pocs.domain.usecase.user

import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class GetMyUserInfoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): UserDetail = repository.getCurrentUserDetail()
}