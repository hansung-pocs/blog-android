package com.pocs.presentation.model.post

import com.pocs.presentation.model.post.item.PostDetailItemUiState

sealed class PostDetailUiState {
    data class Success(
        val postDetail: PostDetailItemUiState,
        val canEditPost: Boolean,
        val canDeletePost: Boolean,
        val isSuccessToDelete: Boolean = false,
        val errorMessage: String? = null
    ) : PostDetailUiState()

    data class Failure(val message: String?) : PostDetailUiState()

    object Loading : PostDetailUiState()
}
