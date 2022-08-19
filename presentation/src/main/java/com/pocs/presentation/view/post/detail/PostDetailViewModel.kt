package com.pocs.presentation.view.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.comment.AddCommentUseCase
import com.pocs.domain.usecase.comment.DeleteCommentUseCase
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
    // post
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val canEditPostUseCase: CanEditPostUseCase,
    private val canDeletePostUseCase: CanDeletePostUseCase,

    // comment
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,

    // user
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

                fetchComments()
            } else {
                val errorMessage = result.exceptionOrNull()!!.message
                _uiState.update { PostDetailUiState.Failure(message = errorMessage) }
            }
        }
    }

    private fun fetchComments() {
        check(_uiState.value is PostDetailUiState.Success)
        commentFetchJob?.cancel()
        commentFetchJob = viewModelScope.launch {
            val uiStateValue = (_uiState.value as PostDetailUiState.Success)
            val result = getCommentsUseCase(postId = uiStateValue.postDetail.id)
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
            _uiState.update { (it as PostDetailUiState.Success).copy(comments = comments) }
        }
    }

    fun addComment(parentId: Int?, content: String) {
        val uiStateValue = uiState.value
        check(uiStateValue is PostDetailUiState.Success)
        val postId = uiStateValue.postDetail.id
        viewModelScope.launch {
            val result = addCommentUseCase(
                content = content,
                postId = postId,
                parentId = parentId
            )
            if (result.isSuccess) {
                fetchComments()
                // TODO: StringRes로 바꾸기
                showUserMessage(message = "댓글 추가됨")
            } else {
                showUserMessage(message = result.exceptionOrNull()?.message ?: "댓글 추가에 실패함") // TODO: StringRes로 바꾸기
            }
        }
    }

    fun updateComment(id: Int, comment: String) {
        // TODO: 구현하기
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            val result = deleteCommentUseCase(commentId = commentId)
            if (result.isSuccess) {
                fetchComments()
                // TODO: StringRes로 바꾸기
                showUserMessage("댓글 삭제됨")
            } else {
                // TODO: StringRes로 바꾸기
                showUserMessage("댓글 삭제에 실패함")
            }
        }
    }

    fun requestPostDeleting(id: Int) {
        viewModelScope.launch {
            val result = deletePostUseCase(postId = id)

            if (result.isSuccess) {
                _uiState.update {
                    (it as PostDetailUiState.Success).copy(isDeleteSuccess = true)
                }
            } else {
                val errorMessage = result.exceptionOrNull()!!.message ?: "게시글 삭제에 실패함" // TODO: StringRes로 바꾸기
                showUserMessage(errorMessage)
            }
        }
    }

    fun showUserMessage(message: String) {
        val uiStateValue = _uiState.value
        check(uiStateValue is PostDetailUiState.Success)
        _uiState.update {
            uiStateValue.copy(userMessage = message)
        }
    }

    fun userMessageShown() {
        val uiStateValue = _uiState.value
        check(uiStateValue is PostDetailUiState.Success)
        _uiState.update {
            uiStateValue.copy(userMessage = null)
        }
    }
}
