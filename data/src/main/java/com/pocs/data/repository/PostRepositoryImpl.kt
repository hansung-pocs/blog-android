package com.pocs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocs.data.api.PostApi
import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toDto
import com.pocs.data.mapper.toEntity
import com.pocs.data.model.post.PostAddBody
import com.pocs.data.model.post.PostDeleteBody
import com.pocs.data.model.post.PostUpdateBody
import com.pocs.data.paging.PostPagingSource
import com.pocs.data.paging.PostPagingSource.Companion.PAGE_SIZE
import com.pocs.data.source.PostRemoteDataSource
import com.pocs.domain.model.post.Post
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostDetail
import com.pocs.domain.model.post.PostFilterType
import com.pocs.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val dataSource: PostRemoteDataSource,
    private val api: PostApi
) : PostRepository {

    override fun getAll(filterType: PostFilterType): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { PostPagingSource(api = api, filterType = filterType) }
        ).flow
    }

    override suspend fun getPostDetail(id: Int): Result<PostDetail> {
        return try {
            val response = dataSource.getPostDetail(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!.data.toEntity(id))
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addPost(
        title: String,
        onlyMember: Boolean,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Unit> {
        return try {
            val response = dataSource.addPost(
                PostAddBody(
                    title = title,
                    onlyMember = onlyMember,
                    content = content,
                    userId = userId,
                    category = category.toDto()
                )
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

    override suspend fun updatePost(
        postId: Int,
        title: String,
        onlyMember: Boolean,
        content: String,
        userId: Int,
        category: PostCategory
    ): Result<Unit> {
        return try {
            val response = dataSource.updatePost(
                postId = postId,
                postUpdateBody = PostUpdateBody(
                    title = title,
                    onlyMember = onlyMember,
                    content = content,
                    userId = userId,
                    category = category.toDto()
                )
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

    override suspend fun deletePost(
        postId: Int,
        userId: Int
    ): Result<Unit> {
        return try {
            val result = dataSource.deletePost(
                postId = postId,
                postDeleteBody = PostDeleteBody(userId = userId)
            )
            if (result.isSuccessful) {
                Result.success(Unit)
            } else {
                throw Exception(result.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
