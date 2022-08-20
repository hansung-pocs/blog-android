package com.pocs.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pocs.data.api.UserApi
import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toDto
import com.pocs.data.mapper.toEntity
import com.pocs.data.model.ResponseBody
import com.pocs.data.model.user.UserListDto
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserListSortingMethod
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class UserPagingSource @Inject constructor(
    private val api: UserApi,
    private val sortingMethod: UserListSortingMethod? = null,
    private val query: String? = null
) : PagingSource<Int, User>() {

    init {
        assert((sortingMethod != null || query != null) && (sortingMethod == null || query == null)) {
            "둘 중 하나는 널이 아니면서 둘 중 하나는 널이어야 합니다."
        }
    }

    companion object {
        private const val START_PAGE = 1
        const val PAGE_SIZE = 15
    }

    private suspend fun requestUserList(page: Int): Response<ResponseBody<UserListDto>> {
        return if (sortingMethod != null) {
            api.getAll(sort = sortingMethod.toDto(), pageSize = PAGE_SIZE, page = page)
        } else if (query != null) {
            api.search(query = query, page = page, pageSize = PAGE_SIZE)
        } else {
            throw IllegalStateException()
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: START_PAGE
        return try {
            val response = requestUserList(page = page)
            if (response.isSuccessful) {
                val users = response.body()!!.data.users.map { it.toEntity() }
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

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}