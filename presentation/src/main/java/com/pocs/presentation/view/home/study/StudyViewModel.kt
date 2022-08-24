package com.pocs.presentation.view.home.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.model.post.PostFilterType
import com.pocs.domain.usecase.post.GetAllPostsUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAnonymousUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.study.StudyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Study 기획이 구체화 되면 업데이트하기. 현재는 임시임
@HiltViewModel
class StudyViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
    isCurrentUserAnonymousUseCase: IsCurrentUserAnonymousUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        StudyUiState(isUserAnonymous = isCurrentUserAnonymousUseCase())
    )
    val uiState = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            getAllPostsUseCase(PostFilterType.STUDY).cachedIn(viewModelScope)
                .map { it.map { post -> post.toUiState() } }
                .collectLatest { pagingData ->
                    _uiState.update {
                        it.copy(pagingData = pagingData)
                    }
                }
        }
    }
}