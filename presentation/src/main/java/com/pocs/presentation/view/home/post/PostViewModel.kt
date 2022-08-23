package com.pocs.presentation.view.home.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.model.post.PostFilterType
import com.pocs.domain.usecase.post.GetAllPostsUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAnonymousUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.post.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
    isCurrentUserAnonymousUseCase: IsCurrentUserAnonymousUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PostUiState(isUserAnonymous = isCurrentUserAnonymousUseCase())
    )
    val uiState = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            getAllPostsUseCase(uiState.value.selectedPostFilterType).cachedIn(viewModelScope)
                .map {
                    it.map { post ->
                        post.toUiState(showCategory = uiState.value.visiblePostCategory)
                    }
                }
                .collectLatest { pagingData ->
                    _uiState.update {
                        it.copy(pagingData = pagingData)
                    }
                }
        }
    }

    fun updatePostFilterType(postFilterType: PostFilterType) {
        _uiState.update { it.copy(selectedPostFilterType = postFilterType) }
        fetchPosts()
    }
}