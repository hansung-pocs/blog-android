package com.pocs.presentation.view.admin.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.usecase.admin.GetAllUsersAsAdmin
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.admin.AdminUserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminUserViewModel @Inject constructor(
    private val getAllUsersAsAdmin: GetAllUsersAsAdmin
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUserUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllUsersAsAdmin()
                .cachedIn(viewModelScope)
                .map { it.map { user -> user.toUiState() } }
                .collectLatest { pagingData ->
                    _uiState.update {
                        it.copy(userPagingData = pagingData)
                    }
                }
        }
    }
}