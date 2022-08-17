package com.pocs.presentation.view.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.comment.GetCommentsUseCase
import com.pocs.domain.usecase.post.CanDeletePostUseCase
import com.pocs.domain.usecase.post.CanEditPostUseCase
import com.pocs.domain.usecase.post.DeletePostUseCase
import com.pocs.domain.usecase.post.GetPostDetailUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.comment.CommentsUiState
import com.pocs.presentation.model.comment.item.CommentItemUiState
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
    private val canDeletePostUseCase: CanDeletePostUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var postFetchJob: Job? = null
    private var commentFetchJob: Job? = null

    fun fetchPost(id: Int) {
        postFetchJob?.cancel()
        postFetchJob = viewModelScope.launch {
            val result = getPostDetailUseCase(id)

            if (result.isSuccess) {
                val postDetail = result.getOrNull()!!
                val previousUiStateValue = uiState.value

                val userMessage = if (previousUiStateValue is PostDetailUiState.Success) {
                    previousUiStateValue.userMessage
                } else null

                val newUiState = PostDetailUiState.Success(
                    postDetail = postDetail.toUiState(),
                    canEditPost = canEditPostUseCase(postDetail),
                    canDeletePost = canDeletePostUseCase(postDetail),
                    userMessage = userMessage,
                )

                _uiState.update { newUiState }

                fetchComments(postId = id, postDetailUiState = newUiState)
            } else {
                val errorMessage = result.exceptionOrNull()!!.message
                _uiState.update { PostDetailUiState.Failure(message = errorMessage) }
            }
        }
    }

    private fun fetchComments(postId: Int, postDetailUiState: PostDetailUiState.Success) {
        commentFetchJob?.cancel()
        commentFetchJob = viewModelScope.launch {
            val result = getCommentsUseCase(postId = postId)
            val comments = if (result.isSuccess) {
                val currentUser = getCurrentUserUseCase()
                val comments = result.getOrNull()!!.map {
                    it.toUiState(
                        currentUserId = currentUser?.id,
                        isAdmin = currentUser?.type == UserType.ADMIN
                    )
                }
                CommentsUiState.Success(comments = comments)
            } else {
                val errorMessage = result.exceptionOrNull()!!.message
                CommentsUiState.Failure(message = errorMessage)
            }

            _uiState.update { postDetailUiState.copy(comments = comments) }
        }
    }

    fun addComment(parentId: Int?, comment: String) {
        // TODO: 구현하기
    }

    fun updateComment(id: Int, comment: String) {
        // TODO: 구현하기
    }

    fun deleteComment(comment: CommentItemUiState) {
        // TODO: 구현하기
    }

    fun requestPostDeleting(id: Int) {
        viewModelScope.launch {
            val result = deletePostUseCase(postId = id)

            if (result.isSuccess) {
                _uiState.update {
                    (it as PostDetailUiState.Success).copy(isDeleteSuccess = true)
                }
            } else {
                val errorMessage = result.exceptionOrNull()!!.message ?: "삭제에 실패했습니다."
                showUserMessage(errorMessage)
            }
        }
    }

    fun showUserMessage(message: String) {
        val uiStateValue = _uiState.value
        require(uiStateValue is PostDetailUiState.Success)
        _uiState.update {
            uiStateValue.copy(userMessage = message)
        }
    }

    fun userMessageShown() {
        val uiStateValue = _uiState.value
        require(uiStateValue is PostDetailUiState.Success)
        _uiState.update {
            uiStateValue.copy(userMessage = null)
        }
    }
}
