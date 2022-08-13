package com.pocs.presentation.mapper

import com.pocs.domain.model.post.Post
import com.pocs.presentation.extension.toFormattedDateString
import com.pocs.presentation.model.post.item.PostItemUiState

fun Post.toUiState(showCategory: Boolean = false) = PostItemUiState(
    id = id,
    title = title,
    content = content,
    category = if (showCategory) category else null,
    writer = writer,
    createdAt = createdAt.toFormattedDateString(),
    canceledAt = canceledAt
)