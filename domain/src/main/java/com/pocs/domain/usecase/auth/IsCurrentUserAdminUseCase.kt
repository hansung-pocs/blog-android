package com.pocs.domain.usecase.auth

import com.pocs.domain.model.user.UserType
import javax.inject.Inject

class IsCurrentUserAdminUseCase @Inject constructor(
    private val getCurrentUserTypeUseCase: GetCurrentUserTypeUseCase
) {
    operator fun invoke() = getCurrentUserTypeUseCase() == UserType.ADMIN
}