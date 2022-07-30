package com.pocs.presentation.model.post.item

import com.pocs.domain.model.post.PostCategory

data class PostDetailItemUiState(
    val id: Int,
    val title: String,
    val content: String,
    val writer: String,
    val date: String,
    val category: PostCategory
)
