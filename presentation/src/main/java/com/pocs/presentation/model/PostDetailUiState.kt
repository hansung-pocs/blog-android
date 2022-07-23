package com.pocs.presentation.model

import com.pocs.domain.model.PostCategory

data class PostDetailUiState(
    val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val writer: String = "",
    val date: String = "",
    val category: PostCategory? = null
)
