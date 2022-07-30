package com.pocs.test_library.fake

import androidx.paging.PagingData
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepositoryImpl: UserRepository {

    override fun getAll(sortingMethod: UserListSortingMethod): Flow<PagingData<User>> {
        return flow {
            PagingData.from<User>(listOf())
        }
    }

    override suspend fun getUserDetail(id: Int): Result<UserDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(
        id: Int,
        password: Int,
        email: String,
        company: String?,
        github: String
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }
}