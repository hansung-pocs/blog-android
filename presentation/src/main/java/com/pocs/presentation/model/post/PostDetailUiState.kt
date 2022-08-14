package com.pocs.presentation.model.post

import com.pocs.presentation.model.post.item.PostDetailItemUiState

sealed class PostDetailUiState {
    data class Success(
        val postDetail: PostDetailItemUiState,
        val canEditPost: Boolean,
        val canDeletePost: Boolean,
        val isDeleteSuccess: Boolean = false,
        val userMessage: String? = null
    ) : PostDetailUiState()

    data class Failure(val message: String?) : PostDetailUiState()

    object Loading : PostDetailUiState()
}
