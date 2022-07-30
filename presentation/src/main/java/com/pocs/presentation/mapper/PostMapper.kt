package com.pocs.presentation.mapper

import com.pocs.domain.model.post.Post
import com.pocs.presentation.model.post.PostItemUiState

fun Post.toUiState() = PostItemUiState(
    id = id,
    title = title,
    writer = writer,
    date = createdAt
)