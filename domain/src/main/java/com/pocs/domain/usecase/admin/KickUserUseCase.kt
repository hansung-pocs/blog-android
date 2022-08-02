package com.pocs.domain.usecase.admin

import com.pocs.domain.repository.AdminRepository
import javax.inject.Inject

class KickUserUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(id: Int) = repository.kickUser(id = id)
}