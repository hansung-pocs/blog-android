package com.pocs.presentation.view.user

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.GetAllUsersUseCase
import com.pocs.domain.usecase.user.SearchUserUseCase
import com.pocs.presentation.R
import com.pocs.presentation.constant.MIN_USER_NAME_SEARCH_LEN
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.user.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    getCurrentUserTypeUseCase: GetCurrentUserTypeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UserUiState(currentUserType = getCurrentUserTypeUseCase())
    )
    val uiState = _uiState.asStateFlow()

    private var fetchJob: Job? = null
    private var searchJob: Job? = null

    init {
        fetchUserList()
    }

    private fun fetchUserList() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            getAllUsersUseCase(uiState.value.sortingMethod)
                .cachedIn(viewModelScope)
                .map { it.map { user -> user.toUiState() } }
                .collectLatest { pagingData ->
                    _uiState.update {
                        it.copy(userPagingData = pagingData)
                    }
                }
        }
    }

    fun search(query: String) {
        if (query.length < MIN_USER_NAME_SEARCH_LEN) {
            showErrorMessage(R.string.query_min_length_error)
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchUserUseCase(query = query, sortingMethod = uiState.value.sortingMethod)
                .cachedIn(viewModelScope)
                .map { it.map { user -> user.toUiState() } }
                .collectLatest { pagingData ->
                    _uiState.update {
                        it.copy(searchPagingData = pagingData)
                    }
                }
        }
    }

    fun updateSortingMethod(sortingMethod: UserListSortingMethod) {
        _uiState.update { it.copy(sortingMethod = sortingMethod) }
        fetchUserList()
    }

    fun onSearchModeChange(enabled: Boolean) {
        _uiState.update {
            val searchPagingData = if (!enabled) {
                // 검색 모드가 종료되면 검색 페이징 데이터를 비운다.
                PagingData.empty()
            } else {
                it.searchPagingData
            }
            it.copy(enabledSearchMode = enabled, searchPagingData = searchPagingData)
        }
    }

    fun errorMessageShown() {
        _uiState.update { it.copy(errorMessageRes = null) }
    }

    private fun showErrorMessage(@StringRes resId: Int) {
        _uiState.update { it.copy(errorMessageRes = resId) }
    }
}
