package com.pocs.presentation.view.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.post.CanDeletePostUseCase
import com.pocs.domain.usecase.post.CanEditPostUseCase
import com.pocs.domain.usecase.post.DeletePostUseCase
import com.pocs.domain.usecase.post.GetPostDetailUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.post.PostDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val canEditPostUseCase: CanEditPostUseCase,
    private val canDeletePostUseCase: CanDeletePostUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchPost(id: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val result = getPostDetailUseCase(id)
            if (result.isSuccess) {
                val postDetail = result.getOrNull()!!
                _uiState.update {
                    PostDetailUiState.Success(
                        postDetail = postDetail.toUiState(),
                        canEditPost = canEditPostUseCase(postDetail),
                        canDeletePost = canDeletePostUseCase(postDetail)
                    )
                }
            } else {
                val errorMessage = result.exceptionOrNull()!!.message
                _uiState.update { PostDetailUiState.Failure(message = errorMessage) }
            }
        }
    }

    fun deletePost(id: Int) {
        viewModelScope.launch {
            deletePostUseCase(
                postId = id,
                // TODO: 현재 접속중인 유저의 ID로 변경하기
                userId = 1
            )
        }
    }
}