package com.pocs.presentation.mapper

import com.pocs.domain.model.Post
import com.pocs.presentation.model.PostItemUiState

fun Post.toUiState() = PostItemUiState(
    id = id,
    title = title,
    writer = writer,
    date = createdAt
)