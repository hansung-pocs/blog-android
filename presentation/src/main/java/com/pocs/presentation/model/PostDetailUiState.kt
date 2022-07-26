package com.pocs.presentation.model

import com.pocs.domain.model.PostCategory

sealed class PostDetailUiState {

    class Success(
        val id: Int,
        val title: String,
        val content: String,
        val writer: String,
        val date: String,
        val category: PostCategory,
    ) : PostDetailUiState()

    class Failure(val message: String?) : PostDetailUiState()

    object Loading : PostDetailUiState()
}
