package com.pocs.presentation.model

import com.pocs.domain.model.PostCategory

data class PostCreateUiState(
    val title: String = "",
    val content: String = "",
    val category: PostCategory,
    val isInSaving: Boolean = false,
    val onTitleChange: (String) -> Unit,
    val onContentChange: (String) -> Unit,
    val onSave: suspend () -> Result<Boolean>
) {
    val canSave get() = title.isNotEmpty() && content.isNotEmpty()
}
