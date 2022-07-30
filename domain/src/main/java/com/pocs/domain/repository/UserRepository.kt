package com.pocs.domain.repository

import androidx.paging.PagingData
import com.pocs.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAll(): Flow<PagingData<User>>

    suspend fun getUser(id: Int): Result<User>

    suspend fun updateUser(
        id: Int,
        password: Int,
        email: String,
        company: String?,
        github: String
    ): Result<Boolean>
}