package com.pocs.presentation.model.post

import com.pocs.domain.model.post.PostCategory

data class PostCreateUiState(
    override val title: String = "",
    override val content: String = "",
    override val category: PostCategory,
    override val isUserAdmin: Boolean,
    override val isInSaving: Boolean = false,
    override val onTitleChange: (String) -> Unit,
    override val onContentChange: (String) -> Unit,
    override val onCategoryChange: (PostCategory) -> Unit,
    override val onSave: suspend () -> Result<Unit>
) : BasePostEditUiState
