package com.pocs.test_library.fake

import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FakeAuthRepositoryImpl @Inject constructor() : AuthRepository {

    var currentUser = MutableStateFlow<UserDetail?>(null)

    override suspend fun login(userName: String, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): StateFlow<UserDetail?> {
        return currentUser
    }
}