package com.pocs.domain.usecase.user

import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: Int) = repository.getUser(id)
}