package com.pocs.presentation.mapper

import com.pocs.domain.model.post.PostWriter
import com.pocs.presentation.model.post.item.PostWriterUiState

fun PostWriter.toUiState() = PostWriterUiState(
    name = name,
    id = id
)