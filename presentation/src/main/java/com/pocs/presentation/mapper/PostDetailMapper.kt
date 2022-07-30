package com.pocs.presentation.mapper

import com.pocs.domain.model.post.PostDetail
import com.pocs.presentation.model.post.PostDetailUiState

fun PostDetail.toSuccessUiState() = PostDetailUiState.Success(
    id = id,
    title = title,
    content = content,
    writer = writer.name,
    date = createdAt,
    category = category
)