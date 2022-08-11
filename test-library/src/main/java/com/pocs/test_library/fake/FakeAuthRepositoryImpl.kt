package com.pocs.test_library.fake

import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.repository.AuthRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FakeAuthRepositoryImpl @Inject constructor() : AuthRepository {

    private var isReadyFlow = MutableSharedFlow<Boolean>()

    var currentUser = MutableStateFlow<UserDetail?>(null)

    var loginResult = Result.success(Unit)

    var logoutResult = Result.success(Unit)

    suspend fun emit(isReady: Boolean) {
        isReadyFlow.emit(isReady)
    }

    override fun isReady(): Flow<Boolean> = isReadyFlow

    override suspend fun login(userName: String, password: String): Result<Unit> {
        return loginResult
    }

    override suspend fun logout(): Result<Unit> {
        return logoutResult
    }

    override fun getCurrentUser(): StateFlow<UserDetail?> {
        return currentUser
    }

    override fun syncCurrentUser(
        name: String,
        email: String,
        company: String?,
        github: String?
    ) {
        currentUser.value = currentUser.value?.copy(
            name = name,
            email = email,
            company = company,
            github = github
        )
    }
}