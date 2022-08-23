package com.pocs.test_library.fake

import androidx.paging.PagingData
import com.pocs.domain.model.user.AnonymousCreateInfo
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeUserRepositoryImpl @Inject constructor() : UserRepository {

    var userPagingFlow = flowOf<PagingData<User>>()

    var searchResult = emptyFlow<PagingData<User>>()

    var userDetailResult: Result<UserDetail> = Result.failure(Exception("Empty"))

    var updateUserResult = Result.success(Unit)

    var createAnonymousResult = Result.success(Unit)

    override fun getAll(sortingMethod: UserListSortingMethod): Flow<PagingData<User>> {
        return userPagingFlow
    }

    override fun search(
        query: String,
        sortingMethod: UserListSortingMethod
    ): Flow<PagingData<User>> {
        return searchResult
    }

    override suspend fun getUserDetail(id: Int) = userDetailResult

    override suspend fun updateUser(
        id: Int,
        password: String,
        name: String,
        email: String,
        company: String?,
        github: String?
    ): Result<Unit> {
        return updateUserResult
    }

    override suspend fun createAnonymous(anonymousCreateInfo: AnonymousCreateInfo): Result<Unit> {
        return createAnonymousResult
    }
}