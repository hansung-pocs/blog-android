package com.pocs.domain.repository

import androidx.paging.PagingData
import com.pocs.domain.model.admin.UserCreateInfo
import com.pocs.domain.model.post.Post
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail
import kotlinx.coroutines.flow.Flow

interface AdminRepository {

    fun getAllUsers(): Flow<PagingData<User>>

    suspend fun getUserDetail(id: Int): Result<UserDetail>

    suspend fun createUser(userCreateInfo: UserCreateInfo): Result<Unit>

    suspend fun kickUser(id: Int): Result<Unit>

    fun getAllPosts(): Flow<PagingData<Post>>

    fun getAllPostsByUser(userId: Int): Flow<PagingData<Post>>
}