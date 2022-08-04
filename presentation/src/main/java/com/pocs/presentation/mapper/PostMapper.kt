package com.pocs.presentation.mapper

import com.pocs.domain.model.post.Post
import com.pocs.presentation.extension.toFormattedDateString
import com.pocs.presentation.model.post.item.PostItemUiState

fun Post.toUiState() = PostItemUiState(
    id = id,
    title = title,
    content = content,
    writer = writer,
    createdAt = createdAt.toFormattedDateString(),
    canceledAt = canceledAt
)