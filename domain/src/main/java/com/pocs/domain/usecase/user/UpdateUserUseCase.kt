package com.pocs.domain.usecase.user

import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        id: Int,
        password: Int,
        email: String,
        company: String?,
        github: String
    ) = repository.updateUser(
        id = id,
        password = password,
        email = email,
        company = company,
        github = github
    )
}