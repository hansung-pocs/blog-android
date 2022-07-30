package com.pocs.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pocs.data.api.UserApi
import com.pocs.data.mapper.toDto
import com.pocs.data.mapper.toEntity
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserListSortingMethod
import java.lang.Exception
import javax.inject.Inject

class UserPagingSource @Inject constructor(
    private val api: UserApi,
    private val sortType: UserListSortingMethod
) : PagingSource<Int, User>() {

    companion object {
        private const val START_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: START_PAGE
        return try {
            val response = api.getAll(sortType.toDto())
            if (response.isSuccess) {
                val users = response.data.users.map { it.toEntity() }
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

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}