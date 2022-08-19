package com.pocs.data.model.comment

data class CommentDto(
    val id: Int,
    val parentId: Int?,
    val childrenCount: Int,
    val postId: Int,
    val writer: CommentWriterDto,
    val content: String,
    val createdAt: String,
    val updatedAt: String? = null,
    val canceledAt: String? = null
)
