package com.pocs.domain.usecase.admin

import com.pocs.domain.model.admin.UserCreateInfo
import com.pocs.domain.repository.AdminRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(
        userCreateInfo: UserCreateInfo
    ) = repository.createUser(userCreateInfo = userCreateInfo)
}