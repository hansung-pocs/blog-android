package com.pocs.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pocs.data.api.AdminApi
import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toEntity
import com.pocs.domain.model.post.Post
import java.lang.Exception
import javax.inject.Inject

class AdminPostPagingSource @Inject constructor(
    private val api: AdminApi
) : PagingSource<Int, Post>() {

    companion object {
        private const val START_PAGE = 1
        const val PAGE_SIZE = 15
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: START_PAGE
        return try {
            val response = api.getAllPosts(pageSize = PAGE_SIZE, page = page)
            if (response.isSuccessful) {
                val users = response.body()!!.data.posts.map { it.toEntity() }
                val isEnd = users.isEmpty()

                LoadResult.Page(
                    data = users,
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
