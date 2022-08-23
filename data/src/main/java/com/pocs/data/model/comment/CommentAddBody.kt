package com.pocs.data.model.comment

data class CommentAddBody(
    val postId: Int,
    val parentId: Int?,
    val content: String
)
