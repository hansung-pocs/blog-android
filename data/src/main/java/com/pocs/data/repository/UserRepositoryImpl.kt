package com.pocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocs.data.api.UserApi
import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toDetailEntity
import com.pocs.data.paging.UserPagingSource
import com.pocs.data.source.UserRemoteDataSource
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.model.user.UserType
import com.pocs.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val dataSource: UserRemoteDataSource
) : UserRepository {

    // TODO: 로그인 기능이 구현되면 실제 로그인한 사용자의 정보로 초기화하기
    private val currentUser = UserDetail(
        1,
        "권김정",
        "abc@google.com",
        1971034,
        UserType.ADMIN,
        "-",
        30,
        "https://github.com/",
        "2021-02-12",
        "-",
    )

    override fun getAll(sortingMethod: UserListSortingMethod): Flow<PagingData<User>> {
        return Pager(
            // TODO: API 페이지네이션 구현되면 페이지 사이즈 수정하기
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { UserPagingSource(api, sortingMethod) }
        ).flow
    }

    override suspend fun getUserDetail(id: Int): Result<UserDetail> {
        return try {
            val response = dataSource.getUserDetail(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!.data.toDetailEntity())
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(
        id: Int,
        password: String,
        name: String,
        email: String,
        company: String?,
        github: String?
    ): Result<Unit> {
        return try {
            val response = dataSource.updateUser(
                id = id,
                password = password,
                name = name,
                email = email,
                company = company,
                github = github
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUserDetail(): UserDetail {
        // TODO: 로그인 기능 API 구현되면 현재 로그인한 유저를 반환하도록 수정하기
        return currentUser
    }
}