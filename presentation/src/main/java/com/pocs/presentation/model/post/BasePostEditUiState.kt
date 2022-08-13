package com.pocs.presentation.model.post

import com.pocs.domain.model.post.PostCategory

interface BasePostEditUiState {
    val title: String
    val content: String
    val category: PostCategory
    val isUserAdmin: Boolean
    val isInSaving: Boolean
    val onTitleChange: (String) -> Unit
    val onContentChange: (String) -> Unit
    val onCategoryChange: (PostCategory) -> Unit
    val onSave: suspend () -> Result<Unit>

    val canSave get() = title.isNotEmpty() && content.isNotEmpty()
    val isEmpty get() = title.isEmpty() && content.isEmpty()
}
