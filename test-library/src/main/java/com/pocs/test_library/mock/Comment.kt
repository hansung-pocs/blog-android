package com.pocs.test_library.mock

import com.pocs.presentation.model.comment.item.CommentItemUiState
import com.pocs.presentation.model.comment.item.CommentWriterUiState

val mockComment = CommentItemUiState(
    id = 10,
    parentId = null,
    childrenCount = 0,
    postId = 1,
    canEdit = true,
    canDelete = true,
    writer = CommentWriterUiState(
        userId = 1,
        name = "홍길동"
    ),
    content = "댓글 내용입니다.",
    date = "오늘"
)