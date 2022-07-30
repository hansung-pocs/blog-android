package com.pocs.presentation.view.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.post.GetPostDetailUseCase
import com.pocs.presentation.mapper.toSuccessUiState
import com.pocs.presentation.model.PostDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchPost(id: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val result = getPostDetailUseCase(id)
            if (result.isSuccess) {
                val data = result.getOrNull()!!
                _uiState.update { data.toSuccessUiState() }
            } else {
                val errorMessage = result.exceptionOrNull()!!.message
                _uiState.update { PostDetailUiState.Failure(message = errorMessage) }
            }
        }
    }
}