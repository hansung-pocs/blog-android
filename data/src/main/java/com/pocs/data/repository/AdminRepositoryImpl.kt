package com.pocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocs.data.api.AdminApi
import com.pocs.data.paging.AdminPagingSource
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val api: AdminApi
): AdminRepository {

    override fun getAllUsers(): Flow<PagingData<User>> {
        return Pager(
            // TODO: API 페이지네이션 구현되면 페이지 사이즈 수정하기
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { AdminPagingSource(api) }
        ).flow
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