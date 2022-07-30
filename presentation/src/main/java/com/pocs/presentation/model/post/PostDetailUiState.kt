package com.pocs.presentation.model.post

import com.pocs.domain.model.post.PostCategory

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
