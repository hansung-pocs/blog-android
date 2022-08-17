package com.pocs.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pocs.data.api.PostApi
import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toDto
import com.pocs.data.mapper.toEntity
import com.pocs.domain.model.post.Post
import com.pocs.domain.model.post.PostFilterType
import java.lang.Exception
import javax.inject.Inject

class PostPagingSource @Inject constructor(
    private val api: PostApi,
    private val filterType: PostFilterType
) : PagingSource<Int, Post>() {

    companion object {
        private const val START_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: START_PAGE
        return try {
            val response = api.getAll(filterType.toDto())
            if (response.isSuccessful) {
                val posts = response.body()!!.data.posts.map { it.toEntity() }
                // TODO: API에서 페이지네이션 구현되면 수정하기
                val isEnd = true

                LoadResult.Page(
                    data = posts,
                    // TODO: API에서 페이지네이션 구현되면 수정하기
                    prevKey = if (page == START_PAGE) null else page - 1,
                    nextKey = if (isEnd) null else page + 1
                )
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}