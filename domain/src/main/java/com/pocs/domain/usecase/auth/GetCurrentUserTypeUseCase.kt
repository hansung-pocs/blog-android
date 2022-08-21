package com.pocs.domain.usecase.auth

import com.pocs.domain.model.user.UserType
import com.pocs.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserTypeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.getCurrentUser().value?.type ?: UserType.비회원
    // TODO : 백엔드에서 바꿔주면 나중에 바꾸기
}