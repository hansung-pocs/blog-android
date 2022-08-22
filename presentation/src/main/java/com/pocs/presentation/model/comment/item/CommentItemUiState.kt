package com.pocs.presentation.model.comment.item

data class CommentItemUiState(
    val id: Int,
    val parentId: Int?,
    val childrenCount: Int,
    val postId: Int,
    val writer: CommentWriterUiState,
    val canEdit: Boolean,
    val canDelete: Boolean,
    val content: String,
    val date: String,
    val isDeleted: Boolean
) {
    val isReply: Boolean get() = id != parentId
    val showMoreInfoButton: Boolean get() = canDelete || canEdit
}
