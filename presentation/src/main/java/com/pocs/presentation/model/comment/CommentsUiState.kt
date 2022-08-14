package com.pocs.presentation.model.comment

import com.pocs.presentation.model.comment.item.CommentItemUiState

sealed class CommentsUiState {

    class Success(
        val comments: List<CommentItemUiState> = emptyList(),
    ) : CommentsUiState()

    class Failure(val message: String?) : CommentsUiState()

    object Loading : CommentsUiState()
}
