package com.pocs.presentation.model.post.item

data class PostItemUiState(
    val id: Int,
    val title: String,
    val content: String,
    val writer: String? = null,
    val createdAt: String,
    val canceledAt: String? = null
) {
    val isDeleted: Boolean get() = canceledAt != null
}
