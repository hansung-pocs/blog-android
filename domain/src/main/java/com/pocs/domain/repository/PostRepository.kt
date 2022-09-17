package com.pocs.domain.repository

import androidx.paging.PagingData
import com.pocs.domain.model.post.Post
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostDetail
import com.pocs.domain.model.post.PostFilterType
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getAll(filterType: PostFilterType): Flow<PagingData<Post>>

    suspend fun getPostDetail(id: Int): Result<PostDetail>

    suspend fun addPost(
        title: String,
        onlyMember: Boolean,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Unit>

    suspend fun updatePost(
        postId: Int,
        title: String,
        onlyMember: Boolean,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Unit>

    suspend fun deletePost(postId: Int, userId: Int): Result<Unit>
}
