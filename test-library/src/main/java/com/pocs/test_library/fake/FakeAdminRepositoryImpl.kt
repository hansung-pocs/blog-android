package com.pocs.test_library.fake

import androidx.paging.PagingData
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.repository.AdminRepository
import com.pocs.test_library.mock.mockKickedUserDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeAdminRepositoryImpl @Inject constructor() : AdminRepository {

    var userList: List<User> = emptyList()

    var userDetailResult = Result.success(mockKickedUserDetail)

    override fun getAllUsers(): Flow<PagingData<User>> = flowOf(PagingData.from(userList))

    override suspend fun getUserDetail(id: Int) = userDetailResult

    override suspend fun createUser(userDetail: UserDetail, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun kickUser(id: Int): Result<Unit> {
        TODO("Not yet implemented")
    }
}