package com.pocs.domain.usecase.user

import com.pocs.domain.model.user.UserType
import javax.inject.Inject

class IsCurrentUserUnknownUseCase @Inject constructor(
    private val getCurrentUserTypeUseCase: GetCurrentUserTypeUseCase
) {
    operator fun invoke() = getCurrentUserTypeUseCase() == UserType.UNKNOWN
}