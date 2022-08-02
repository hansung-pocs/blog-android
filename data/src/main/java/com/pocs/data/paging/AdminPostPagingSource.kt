package com.pocs.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pocs.data.api.AdminApi
import com.pocs.data.mapper.toEntity
import com.pocs.domain.model.post.Post
import java.lang.Exception
import javax.inject.Inject

class AdminPostPagingSource @Inject constructor(
    private val api: AdminApi
) : PagingSource<Int, Post>() {

    companion object {
        private const val START_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: START_PAGE
        return try {
            val response = api.getAllPosts()
            if (response.isSuccess) {
                val users = response.data.posts.map { it.toEntity() }
                // TODO: API에서 페이지네이션 구현되면 수정하기
                val isEnd = true

                LoadResult.Page(
                    data = users,
                    // TODO: API에서 페이지네이션 구현되면 수정하기
                    prevKey = if (page == START_PAGE) null else page - 1,
                    nextKey = if (isEnd) null else page + 1
                )
            } else {
                throw Exception(response.message)
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
