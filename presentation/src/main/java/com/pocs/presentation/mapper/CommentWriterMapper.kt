package com.pocs.presentation.mapper

import com.pocs.domain.model.comment.CommentWriter
import com.pocs.presentation.model.comment.item.CommentWriterUiState

fun CommentWriter.toUiState() = CommentWriterUiState(
    userId = userId,
    name = name
)
