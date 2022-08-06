package com.pocs.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pocs.data.api.AdminApi
import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toEntity
import com.pocs.domain.model.post.Post
import java.lang.Exception
import javax.inject.Inject

class AdminPostByUserPagingSource @Inject constructor(
    private val api: AdminApi,
    private val userId: Int
) : PagingSource<Int, Post>() {

    companion object {
        private const val START_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: START_PAGE
        return try {
            val response = api.getAllPostsByUser(userId)
            if (response.isSuccessful) {
                val users = response.body()!!.data.posts.map { it.toEntity() }
                // TODO: API에서 페이지네이션 구현되면 수정하기
                val isEnd = true

                LoadResult.Page(
                    data = users,
                    // TODO: API에서 페이지네이션 구현되면 수정하기
                    prevKey = if (page == START_PAGE) null else page - 1,
                    nextKey = if (isEnd) null else page + 1
                )
            } else {
                // 찾을 수 없는 경우 빈 리스트를 반환한다.
                if (response.code() == 404) {
                    return LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }
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
