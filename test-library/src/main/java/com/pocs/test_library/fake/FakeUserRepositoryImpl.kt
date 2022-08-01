package com.pocs.test_library.fake

import androidx.paging.PagingData
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.model.user.UserType
import com.pocs.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeUserRepositoryImpl @Inject constructor() : UserRepository {

    var userDetailResult: Result<UserDetail> = Result.failure(Exception("Empty"))

    var userType: UserType = UserType.MEMBER

    override fun getAll(sortingMethod: UserListSortingMethod): Flow<PagingData<User>> {
        return flow {
            PagingData.from<User>(listOf())
        }
    }

    override suspend fun getUserDetail(id: Int) = userDetailResult

    override suspend fun updateUser(
        id: Int,
        password: String,
        name: String,
        email: String,
        company: String,
        github: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getCurrentUserType() = userType
}