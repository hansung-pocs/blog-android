package com.pocs.presentation.view.home.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.usecase.post.GetAllPostsUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserUnknownUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.post.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
    isCurrentUserUnknownUseCase: IsCurrentUserUnknownUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PostUiState(visiblePostWriteFab = !isCurrentUserUnknownUseCase())
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // TODO: 아래 전달하는 카테고리는 전혀 의미 없는 임시 카테고리임. 현재 벡엔드에서 모든 유형을 한 번에 반환하고 있어서임
            val flow = getAllPostsUseCase(PostCategory.NOTICE)
            flow.cachedIn(viewModelScope)
                .map { it.map { post -> post.toUiState() } }
                .collectLatest { pagingData ->
                    _uiState.update {
                        it.copy(pagingData = pagingData)
                    }
                }
        }
    }
}