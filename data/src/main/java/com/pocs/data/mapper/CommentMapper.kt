package com.pocs.data.mapper

import com.pocs.data.model.comment.CommentDto
import com.pocs.data.model.comment.CommentWriterDto
import com.pocs.domain.model.comment.Comment
import com.pocs.domain.model.comment.CommentWriter

fun CommentDto.toEntity() = Comment(
    id = commentId,
    parentId = parentId,
    childrenCount = childrenCount,
    postId = postId,
    writer = writer.toEntity(),
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    canceledAt = canceledAt
)

fun CommentWriterDto.toEntity() = CommentWriter(
    name = name,
    userId = userId
)