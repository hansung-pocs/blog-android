package com.pocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocs.data.api.UserApi
import com.pocs.data.paging.UserPagingSource
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
): UserRepository {

    override fun getAll(sortingMethod: UserListSortingMethod): Flow<PagingData<User>> {
        return Pager(
            // TODO: API 페이지네이션 구현되면 페이지 사이즈 수정하기
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { UserPagingSource(api, sortingMethod) }
        ).flow
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