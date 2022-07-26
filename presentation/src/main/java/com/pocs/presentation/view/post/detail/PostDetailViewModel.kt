package com.pocs.presentation.view.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.post.GetPostDetailUseCase
import com.pocs.presentation.model.PostDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadArticle(id: Int) {
        viewModelScope.launch {
            val result = getPostDetailUseCase(id)
            if (result.isSuccess) {
                val data = result.getOrNull()!!
                _uiState.update {
                    PostDetailUiState.Success(
                        id = id,
                        title = data.title,
                        content = data.content,
                        writer = data.writer.name,
                        date = data.createdAt,
                        category = data.category
                    )
                }
            } else {
                val errorMessage = result.exceptionOrNull()!!.message
                _uiState.update { PostDetailUiState.Failure(message = errorMessage) }
            }
        }
    }
}