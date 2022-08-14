package com.pocs.presentation.model.comment.item

data class CommentItemUiState(
    val id: Int,
    val parentId: Int?,
    val childrenCount: Int,
    val postId: Int,
    val writer: CommentWriterUiState,
    val isMyComment: Boolean,
    val content: String,
    val date: String
)
