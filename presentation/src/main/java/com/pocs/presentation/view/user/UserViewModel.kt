package com.pocs.presentation.view.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.usecase.user.GetAllUsersUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.user.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    getAllUsersUseCase: GetAllUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
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
}