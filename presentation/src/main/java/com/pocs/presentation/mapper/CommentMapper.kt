package com.pocs.presentation.mapper

import com.pocs.domain.model.comment.Comment
import com.pocs.presentation.extension.createFormattedDateText
import com.pocs.presentation.model.comment.item.CommentItemUiState

fun Comment.toUiState(currentUserId: Int?, isAdmin: Boolean): CommentItemUiState {
    val isMine = currentUserId == writer.userId

    return CommentItemUiState(
        id = id,
        parentId = parentId,
        childrenCount = childrenCount,
        postId = postId,
        writer = writer.toUiState(),
        canEdit = isMine,
        canDelete = isMine || isAdmin,
        content = content,
        date = createFormattedDateText(createdAt, updatedAt),
        isDeleted = canceledAt != null
    )
}
