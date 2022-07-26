package com.pocs.presentation.model

import com.pocs.domain.model.PostCategory

data class PostCreateUiState(
    override val title: String = "",
    override val content: String = "",
    override val category: PostCategory,
    override val isInSaving: Boolean = false,
    override val onTitleChange: (String) -> Unit,
    override val onContentChange: (String) -> Unit,
    override val onSave: suspend () -> Result<Boolean>
) : BasePostEditUiState
