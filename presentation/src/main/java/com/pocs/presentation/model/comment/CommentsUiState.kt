package com.pocs.presentation.model.comment

import com.pocs.presentation.model.comment.item.CommentItemUiState

sealed class CommentsUiState {

    data class Success(
        val comments: List<CommentItemUiState> = emptyList(),
        val canAddComment: Boolean
    ) : CommentsUiState()

    class Failure(val message: String?) : CommentsUiState()

    object Loading : CommentsUiState()
}
