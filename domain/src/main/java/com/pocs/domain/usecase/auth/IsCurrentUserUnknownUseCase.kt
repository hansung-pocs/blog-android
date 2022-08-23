package com.pocs.domain.usecase.auth

import com.pocs.domain.model.user.UserType
import javax.inject.Inject

class IsCurrentUserUnknownUseCase @Inject constructor(
    private val getCurrentUserTypeUseCase: GetCurrentUserTypeUseCase
) {
    operator fun invoke() = getCurrentUserTypeUseCase() == UserType.ANONYMOUS
    // TODO : 백엔드에서 바꿔주면 나중에 바꾸기
}