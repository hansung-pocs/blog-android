package com.pocs.domain.usecase.auth

import com.pocs.domain.repository.AuthRepository
import javax.inject.Inject

class IsAuthReadyUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.isReady()
}