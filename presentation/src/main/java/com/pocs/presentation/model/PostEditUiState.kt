package com.pocs.presentation.model

import com.pocs.domain.model.PostCategory

data class PostEditUiState(
    val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val category: PostCategory? = null,
    val isInSaving: Boolean = false,
    val onChangeTitle: (String) -> Unit,
    val onChangeContent: (String) -> Unit,
    val onSave: suspend () -> Result<Boolean>
) {
    val canSave get() = title.isNotEmpty() && content.isNotEmpty()
}
