package com.pocs.domain.repository

import com.pocs.domain.model.user.UserDetail
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun login(userName: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    fun getCurrentUser(): StateFlow<UserDetail?>
}