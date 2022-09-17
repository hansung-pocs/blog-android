package com.pocs.domain.usecase.auth

import com.pocs.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userName: String, password: String) = authRepository.login(
        userName = userName,
        password = password
    )
}
