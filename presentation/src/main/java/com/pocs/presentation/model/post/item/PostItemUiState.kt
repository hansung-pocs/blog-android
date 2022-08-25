package com.pocs.presentation.model.post.item

import com.pocs.domain.model.post.PostCategory

data class PostItemUiState(
    val id: Int,
    val title: String,
    val content: String,
    val category: PostCategory,
    val displayCategory: Boolean,
    val writer: String? = null,
    val createdAt: String,
    val canceledAt: String? = null,
) {
    val isDeleted: Boolean get() = canceledAt != null
}
