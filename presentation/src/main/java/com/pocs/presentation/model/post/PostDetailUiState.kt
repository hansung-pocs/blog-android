package com.pocs.presentation.model.post

import com.pocs.presentation.model.post.item.PostDetailItemUiState

sealed class PostDetailUiState {

    class Success(val postDetail: PostDetailItemUiState) : PostDetailUiState()

    class Failure(val message: String?) : PostDetailUiState()

    object Loading : PostDetailUiState()
}
