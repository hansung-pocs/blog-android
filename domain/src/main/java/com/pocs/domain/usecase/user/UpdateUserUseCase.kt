package com.pocs.domain.usecase.user

import com.pocs.domain.repository.AuthRepository
import com.pocs.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        id: Int,
        password: String?,
        name: String,
        email: String,
        company: String?,
        github: String?,
        profileImageUrl: String?
    ): Result<Unit> {
        require(id == authRepository.getCurrentUser().value?.id) { "자신의 정보만 수정할 수 있습니다." }

        val result = userRepository.updateUser(
            id = id,
            password = password,
            name = name,
            email = email,
            company = company,
            github = github,
            profileImageUrl = profileImageUrl
        )
        if (result.isSuccess) {
            authRepository.syncCurrentUser(
                name = name,
                email = email,
                company = company,
                github = github
            )
        }

        return result
    }
}