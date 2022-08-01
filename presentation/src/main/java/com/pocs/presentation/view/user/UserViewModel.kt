package com.pocs.presentation.view.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.usecase.user.GetAllUsersUseCase
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
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
    getCurrentUserTypeUseCase: GetCurrentUserTypeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UserUiState(currentUserType = getCurrentUserTypeUseCase())
    )
    val uiState = _uiState.asStateFlow()

    private var fetchJob: Job? = null

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

    fun updateSortingMethod(sortingMethod: UserListSortingMethod) {
        _uiState.update { it.copy(sortingMethod = sortingMethod) }
        fetchUserList()
    }
}