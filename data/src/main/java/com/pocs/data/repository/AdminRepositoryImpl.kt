package com.pocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocs.data.api.AdminApi
import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toDetailEntity
import com.pocs.data.mapper.toDto
import com.pocs.data.model.admin.UserKickInfoBody
import com.pocs.data.paging.AdminPostPagingSource
import com.pocs.data.paging.AdminPostByUserPagingSource
import com.pocs.data.paging.AdminPostByUserPagingSource.Companion.PAGE_SIZE
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
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { AdminUserPagingSource(api) }
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

    override suspend fun createUser(userCreateInfo: UserCreateInfo): Result<Unit> {
        return try {
            val response = dataSource.createUser(userCreateInfo.toDto())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
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
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { AdminPostPagingSource(api) }
        ).flow
    }

    override fun getAllPostsByUser(userId: Int): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { AdminPostByUserPagingSource(api, userId = userId) }
        ).flow
    }

    private fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-DD HH:MM:ss", Locale.getDefault())
        return formatter.format(Date())
    }
}