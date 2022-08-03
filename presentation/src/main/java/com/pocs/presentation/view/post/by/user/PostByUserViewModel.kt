package com.pocs.presentation.view.post.by.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.usecase.admin.GetAllPostsByUserUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.post.PostByUserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostByUserViewModel @Inject constructor(
    private val getAllPostsByUserUseCase: GetAllPostsByUserUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PostByUserUiState())
    val uiState = _uiState.asStateFlow()

    private var job: Job? = null

    fun fetchPosts(userId: Int) {
        job?.cancel()
        job = viewModelScope.launch {
            val flow = getAllPostsByUserUseCase(userId = userId)
            flow.cachedIn(viewModelScope)
                .map { it.map { post -> post.toUiState() } }
                .collectLatest { pagingData ->
                    _uiState.update {
                        it.copy(postPagingData = pagingData)
                    }
                }
        }
    }
}