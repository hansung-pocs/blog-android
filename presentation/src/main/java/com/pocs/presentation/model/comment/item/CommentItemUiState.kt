package com.pocs.presentation.model.comment.item

data class CommentItemUiState(
    val id: Int,
    val parentId: Int?,
    val childrenCount: Int,
    val postId: Int,
    val writer: CommentWriterUiState,
    val isMyComment: Boolean,
    val content: String,
    private val createdAt: String,
    private val updatedAt: String? = null,
    private val canceledAt: String? = null
) {
    val isDeleted: Boolean get() = canceledAt != null

    val time: String
        get() {
            if (updatedAt != null) {
                return "$updatedAt(수정됨)"
            }
            return createdAt
        }
}
