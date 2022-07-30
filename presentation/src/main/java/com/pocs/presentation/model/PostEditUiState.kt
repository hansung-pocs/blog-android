package com.pocs.presentation.model

import com.pocs.domain.model.post.PostCategory

data class PostEditUiState(
    val id: Int,
    override val title: String,
    override val content: String,
    override val category: PostCategory,
    override val isInSaving: Boolean = false,
    override val onTitleChange: (String) -> Unit,
    override val onContentChange: (String) -> Unit,
    override val onSave: suspend () -> Result<Unit>
) : BasePostEditUiState
