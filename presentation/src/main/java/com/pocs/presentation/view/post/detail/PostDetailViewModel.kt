package com.pocs.presentation.view.post.detail

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.comment.AddCommentUseCase
import com.pocs.domain.usecase.comment.DeleteCommentUseCase
import com.pocs.domain.usecase.comment.GetCommentsUseCase
import com.pocs.domain.usecase.comment.UpdateCommentUseCase
import com.pocs.domain.usecase.post.CanDeletePostUseCase
import com.pocs.domain.usecase.post.CanEditPostUseCase
import com.pocs.domain.usecase.post.DeletePostUseCase
import com.pocs.domain.usecase.post.GetPostDetailUseCase
import com.pocs.presentation.R
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.comment.CommentsUiState
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
    private val updateCommentUseCase: UpdateCommentUseCase,
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
                val userMessageRes = if (previousUiStateValue is PostDetailUiState.Success) {
                    previousUiStateValue.userMessageRes
                } else null

                val newUiState = PostDetailUiState.Success(
                    postDetail = postDetail.toUiState(),
                    canEditPost = canEditPostUseCase(postDetail),
                    canDeletePost = canDeletePostUseCase(postDetail),
                    userMessage = userMessage,
                    userMessageRes = userMessageRes
                )

                _uiState.update { newUiState }

                fetchComments()
            } else {
                val exception = result.exceptionOrNull()!!
                _uiState.update { PostDetailUiState.Failure(exception = exception) }
            }
        }
    }

    private fun fetchComments() {
        val uiStateValue = _uiState.value
        check(uiStateValue is PostDetailUiState.Success)
        commentFetchJob?.cancel()
        commentFetchJob = viewModelScope.launch {
            val result = getCommentsUseCase(postId = uiStateValue.postDetail.id)
            val comments = if (result.isSuccess) {
                val currentUser = getCurrentUserUseCase()
                val postDetail = uiStateValue.postDetail
                val canAddComment = when (currentUser?.type) {
                    UserType.ADMIN -> true
                    UserType.MEMBER -> true
                    // 익명 회원은 본인이 작성한 QnA 게시글에서만 댓글을 작성할 수 있다.
                    UserType.ANONYMOUS ->
                        postDetail.writer.id == currentUser.id &&
                            postDetail.category == PostCategory.QNA
                    null -> false
                }
                val comments = result.getOrNull()!!.map {
                    it.toUiState(
                        currentUserId = currentUser?.id,
                        isAdmin = currentUser?.type == UserType.ADMIN
                    )
                }
                CommentsUiState.Success(comments = comments, canAddComment = canAddComment)
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
                showUserMessage(resource = R.string.comment_added)
            } else {
                val errorMessage = result.exceptionOrNull()?.message
                if (errorMessage == null) {
                    showUserMessage(resource = R.string.failed_to_add_comment)
                } else {
                    showUserMessage(message = errorMessage)
                }
            }
        }
    }

    fun updateComment(id: Int, content: String) {
        viewModelScope.launch {
            val result = updateCommentUseCase(commentId = id, content = content)
            if (result.isSuccess) {
                fetchComments()
                showUserMessage(resource = R.string.comment_edited)
            } else {
                val errorMessage = result.exceptionOrNull()?.message
                if (errorMessage == null) {
                    showUserMessage(resource = R.string.failed_to_edit_comment)
                } else {
                    showUserMessage(message = errorMessage)
                }
            }
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            val result = deleteCommentUseCase(commentId = commentId)
            if (result.isSuccess) {
                fetchComments()
                showUserMessage(resource = R.string.comment_deleted)
            } else {
                val errorMessage = result.exceptionOrNull()?.message
                if (errorMessage == null) {
                    showUserMessage(resource = R.string.failed_to_delete_comment)
                } else {
                    showUserMessage(message = errorMessage)
                }
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
                val errorMessage = result.exceptionOrNull()?.message
                if (errorMessage == null) {
                    showUserMessage(resource = R.string.failed_to_delete_post)
                } else {
                    showUserMessage(message = errorMessage)
                }
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

    private fun showUserMessage(@StringRes resource: Int) {
        val uiStateValue = _uiState.value
        check(uiStateValue is PostDetailUiState.Success)
        _uiState.update {
            uiStateValue.copy(userMessageRes = resource)
        }
    }

    fun userMessageShown() {
        val uiStateValue = _uiState.value
        check(uiStateValue is PostDetailUiState.Success)
        _uiState.update {
            uiStateValue.copy(userMessage = null, userMessageRes = null)
        }
    }
}
