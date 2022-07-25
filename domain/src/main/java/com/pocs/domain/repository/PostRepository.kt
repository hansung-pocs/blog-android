package com.pocs.domain.repository

import androidx.paging.PagingData
import com.pocs.domain.model.PostDetail
import com.pocs.domain.model.PostCategory
import com.pocs.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getAll(category: PostCategory): Flow<PagingData<Post>>

    suspend fun getPostDetail(id: Int): Result<PostDetail>

    suspend fun addPost(
        title: String,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Boolean>

    suspend fun updatePost(
        id: Int,
        title: String,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Boolean>

    suspend fun deletePost(postId: Int, userId: Int): Result<Boolean>
}