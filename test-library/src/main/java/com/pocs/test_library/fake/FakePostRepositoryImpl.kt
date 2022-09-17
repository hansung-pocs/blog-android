package com.pocs.test_library.fake

import androidx.paging.PagingData
import com.pocs.domain.model.post.Post
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostDetail
import com.pocs.domain.model.post.PostFilterType
import com.pocs.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class FakePostRepositoryImpl @Inject constructor() : PostRepository {

    var postPagingDataFlow = MutableSharedFlow<PagingData<Post>>()

    var postDetailResult: Result<PostDetail> = Result.failure(Exception())

    var updatePostResult = Result.success(Unit)

    var deletePostResult = Result.success(Unit)

    override fun getAll(filterType: PostFilterType) = postPagingDataFlow

    override suspend fun getPostDetail(id: Int): Result<PostDetail> {
        return postDetailResult
    }

    override suspend fun addPost(
        title: String,
        onlyMember: Boolean,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updatePost(
        postId: Int,
        title: String,
        onlyMember: Boolean,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Unit> {
        return updatePostResult
    }

    override suspend fun deletePost(postId: Int, userId: Int) = deletePostResult
}
