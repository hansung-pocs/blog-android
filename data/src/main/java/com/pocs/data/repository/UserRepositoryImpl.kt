package com.pocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocs.data.api.UserApi
import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toDetailEntity
import com.pocs.data.mapper.toDto
import com.pocs.data.paging.UserPagingSource
import com.pocs.data.paging.UserPagingSource.Companion.PAGE_SIZE
import com.pocs.data.source.UserRemoteDataSource
import com.pocs.domain.model.user.AnonymousCreateInfo
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val dataSource: UserRemoteDataSource
) : UserRepository {

    override fun getAll(sortingMethod: UserListSortingMethod): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { UserPagingSource(api = api, sortingMethod = sortingMethod) }
        ).flow
    }

    override fun search(
        query: String,
        sortingMethod: UserListSortingMethod
    ): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                UserPagingSource(
                    api = api,
                    query = query,
                    sortingMethod = sortingMethod
                )
            }
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

    override suspend fun createAnonymous(anonymousCreateInfo: AnonymousCreateInfo): Result<Unit> {
        return try {
            val response = dataSource.createAnonymous(anonymousCreateInfo.toDto())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}