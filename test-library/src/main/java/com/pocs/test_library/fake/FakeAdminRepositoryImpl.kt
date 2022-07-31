package com.pocs.test_library.fake

import androidx.paging.PagingData
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeAdminRepositoryImpl @Inject constructor() : AdminRepository {

    override fun getAllUsers(): Flow<PagingData<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDetail(id: Int): Result<UserDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(userDetail: UserDetail, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun kickUser(id: Int): Result<Unit> {
        TODO("Not yet implemented")
    }
}