package com.pocs.presentation.mapper

import com.pocs.domain.model.comment.Comment
import com.pocs.presentation.extension.createFormattedDateText
import com.pocs.presentation.model.comment.item.CommentItemUiState

fun Comment.toUiState(currentUserId: Int?) = CommentItemUiState(
    id = id,
    parentId = parentId,
    childrenCount = childrenCount,
    postId = postId,
    writer = writer.toUiState(),
    isMyComment = currentUserId == writer.userId,
    content = content,
    date = createFormattedDateText(createdAt, updatedAt)
)