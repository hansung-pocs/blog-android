package com.pocs.domain.repository

import androidx.paging.PagingData
import com.pocs.domain.model.user.AnonymousCreateInfo
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserListSortingMethod
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAll(sortingMethod: UserListSortingMethod): Flow<PagingData<User>>

    fun search(query: String, sortingMethod: UserListSortingMethod): Flow<PagingData<User>>

    suspend fun getUserDetail(id: Int): Result<UserDetail>

    suspend fun updateUser(
        id: Int,
        password: String,
        name: String,
        email: String,
        company: String?,
        github: String?
    ): Result<Unit>

    suspend fun createAnonymous(anonymousCreateInfo: AnonymousCreateInfo): Result<Unit>
}