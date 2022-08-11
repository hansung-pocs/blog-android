package com.pocs.domain.repository

import com.pocs.domain.model.user.UserDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    fun isReady(): Flow<Boolean>
    suspend fun login(userName: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    fun getCurrentUser(): StateFlow<UserDetail?>
    fun syncCurrentUser(
        name: String,
        email: String,
        company: String?,
        github: String?
    )
}