package com.pocs.domain.usecase.user

import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserTypeUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    operator fun invoke() = repository.getCurrentUserType()
}