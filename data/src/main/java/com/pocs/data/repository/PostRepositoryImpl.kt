package com.pocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocs.data.api.PostApi
import com.pocs.data.mapper.toEntity
import com.pocs.data.paging.PostPagingSource
import com.pocs.data.source.PostRemoteDataSource
import com.pocs.domain.model.Post
import com.pocs.domain.model.PostCategory
import com.pocs.domain.model.PostDetail
import com.pocs.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val dataSource: PostRemoteDataSource,
    private val api: PostApi
) : PostRepository {

    override fun getAll(category: PostCategory): Flow<PagingData<Post>> {
        return Pager(
            // TODO: API 페이지네이션 구현되면 페이지 사이즈 수정하기
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { PostPagingSource(api) }
        ).flow
    }

    override suspend fun getPostDetail(id: Int): Result<PostDetail> {
        return try {
            val response = dataSource.getPostDetail(id)
            if (response.isSuccess) {
                Result.success(response.data.toEntity(id))
            } else {
                throw Exception(response.message)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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