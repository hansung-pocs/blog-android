package com.pocs.presentation.view.home.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.pocs.domain.model.post.PostFilterType
import com.pocs.domain.usecase.post.GetAllPostsUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.question.QuestionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionUiState())
    val uiState = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        fetchQuestions()
    }

    private fun fetchQuestions() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            getAllPostsUseCase(PostFilterType.QNA).cachedIn(viewModelScope)
                .map { it.map { post -> post.toUiState() } }
                .collectLatest { pagingData ->
                    _uiState.update {
                        it.copy(pagingData = pagingData)
                    }
                }
        }
    }
}
