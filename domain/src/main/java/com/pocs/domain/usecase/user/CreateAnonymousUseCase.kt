package com.pocs.domain.usecase.user

import com.pocs.domain.model.user.AnonymousCreateInfo
import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class CreateAnonymousUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        anonymousCreateInfo: AnonymousCreateInfo
    ) = repository.createAnonymous(anonymousCreateInfo = anonymousCreateInfo)
}