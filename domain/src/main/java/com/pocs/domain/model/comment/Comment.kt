package com.pocs.domain.model.comment

data class Comment(
    val id: Int,
    val parentId: Int?,
    val childrenCount: Int,
    val postId: Int,
    val writer: CommentWriter,
    val content: String,
    val createdAt: String,
    val updatedAt: String? = null,
    val canceledAt: String? = null
)
