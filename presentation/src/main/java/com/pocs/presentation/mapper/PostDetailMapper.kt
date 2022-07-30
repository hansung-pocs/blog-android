package com.pocs.presentation.mapper

import com.pocs.domain.model.post.PostDetail
import com.pocs.presentation.model.post.item.PostDetailItemUiState

fun PostDetail.toUiState() = PostDetailItemUiState(
    id = id,
    title = title,
    content = content,
    writer = writer.name,
    date = createdAt,
    category = category
)