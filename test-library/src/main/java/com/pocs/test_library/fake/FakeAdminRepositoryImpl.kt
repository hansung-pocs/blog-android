package com.pocs.test_library.fake

import androidx.paging.PagingData
import com.pocs.domain.model.admin.UserCreateInfo
import com.pocs.domain.model.post.Post
import com.pocs.domain.model.user.User
import com.pocs.domain.repository.AdminRepository
import com.pocs.test_library.mock.mockKickedUserDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeAdminRepositoryImpl @Inject constructor() : AdminRepository {

    var userList: List<User> = emptyList()

    var userDetailResult = Result.success(mockKickedUserDetail)

    var kickUserResult = Result.success(Unit)

    var postList: List<Post> = emptyList()

    override fun getAllUsers(): Flow<PagingData<User>> = flowOf(PagingData.from(userList))

    override suspend fun getUserDetail(id: Int) = userDetailResult

    override suspend fun createUser(userCreateInfo: UserCreateInfo): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun kickUser(id: Int) = kickUserResult

    override fun getAllPosts(): Flow<PagingData<Post>> = flowOf(PagingData.from(postList))
}