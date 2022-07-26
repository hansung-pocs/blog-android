package com.pocs.presentation.fake

import androidx.paging.PagingData
import com.pocs.domain.model.Post
import com.pocs.domain.model.PostCategory
import com.pocs.domain.model.PostDetail
import com.pocs.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow

class FakePostRepositoryImpl: PostRepository {

    var postDetailResult : Result<PostDetail> = Result.failure(Exception())

    override fun getAll(category: PostCategory): Flow<PagingData<Post>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostDetail(id: Int): Result<PostDetail> {
        return postDetailResult
    }

    override suspend fun addPost(
        title: String,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePost(
        id: Int,
        title: String,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePost(postId: Int, userId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }
}