package com.pocs.domain.repository

import androidx.paging.PagingData
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import kotlinx.coroutines.flow.Flow

interface AdminRepository {

    fun getAllUsers(): Flow<PagingData<User>>

    suspend fun getUserDetail(id: Int): Result<UserDetail>

    suspend fun createUser(
        userDetail: UserDetail,
        password: String
    ): Result<Unit>

    suspend fun kickUser(id: Int): Result<Unit>
}