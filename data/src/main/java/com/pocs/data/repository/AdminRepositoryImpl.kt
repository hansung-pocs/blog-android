package com.pocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocs.data.api.AdminApi
import com.pocs.data.mapper.toDetailEntity
import com.pocs.data.mapper.toDto
import com.pocs.data.model.admin.UserKickInfoBody
import com.pocs.data.paging.AdminPostPagingSource
import com.pocs.data.paging.AdminUserPagingSource
import com.pocs.data.source.AdminRemoteDataSource
import com.pocs.domain.model.admin.UserCreateInfo
import com.pocs.domain.model.post.Post
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val api: AdminApi,
    private val dataSource: AdminRemoteDataSource
) : AdminRepository {

    override fun getAllUsers(): Flow<PagingData<User>> {
        return Pager(
            // TODO: API 페이지네이션 구현되면 페이지 사이즈 수정하기
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { AdminUserPagingSource(api) }
        ).flow
    }

    override suspend fun getUserDetail(id: Int): Result<UserDetail> {
        return try {
            val response = dataSource.getUserDetail(id)
            if (response.isSuccess) {
                Result.success(response.data.toDetailEntity())
            } else {
                throw Exception(response.message)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUser(userCreateInfo: UserCreateInfo): Result<Unit> {
        return try {
            val response = dataSource.createUser(userCreateInfo.toDto())
            if (response.isSuccess) {
                Result.success(Unit)
            } else {
                throw Exception(response.message)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun kickUser(id: Int): Result<Unit> {
        return try {
            val response = dataSource.kickUser(
                id = id,
                userKickInfoBody = UserKickInfoBody(canceledAt = getCurrentDateTime())
            )
            if (response.isSuccess) {
                Result.success(Unit)
            } else {
                throw Exception(response.message)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllPosts(): Flow<PagingData<Post>> {
        return Pager(
            // TODO: API 페이지네이션 구현되면 페이지 사이즈 수정하기
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { AdminPostPagingSource(api) }
        ).flow
    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-DD HH:MM:ss", Locale.getDefault())
        return formatter.format(Date())
    }
}