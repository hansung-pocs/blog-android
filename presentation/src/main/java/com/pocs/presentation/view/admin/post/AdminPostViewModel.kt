package com.pocs.presentation.view.admin.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.usecase.admin.GetAllPostsAsAdminUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.admin.AdminPostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPostViewModel @Inject constructor(
    getAllPostsAsAdminUseCase: GetAllPostsAsAdminUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminPostUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllPostsAsAdminUseCase()
                .cachedIn(viewModelScope)
                .map { it.map { post -> post.toUiState() } }
                .collectLatest { pagingData ->
                    _uiState.update { uiState ->
                        uiState.copy(postPagingData = pagingData)
                    }
                }
        }
    }
}