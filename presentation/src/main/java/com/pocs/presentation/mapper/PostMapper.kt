package com.pocs.presentation.mapper

import com.pocs.domain.model.post.Post
import com.pocs.presentation.extension.toFormattedDateString
import com.pocs.presentation.model.post.item.PostItemUiState

fun Post.toUiState(displayCategory: Boolean = false) = PostItemUiState(
    id = id,
    title = title,
    content = content,
    category = category,
    displayCategory = displayCategory,
    writer = writer,
    createdAt = createdAt.toFormattedDateString(),
    canceledAt = canceledAt
)