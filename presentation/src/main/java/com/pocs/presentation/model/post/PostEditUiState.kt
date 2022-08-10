package com.pocs.presentation.model.post

import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.model.BasePostEditUiState

data class PostEditUiState(
    val postId: Int,
    override val title: String,
    override val content: String,
    override val category: PostCategory,
    override val isUserAdmin: Boolean,
    override val isInSaving: Boolean = false,
    override val onTitleChange: (String) -> Unit,
    override val onContentChange: (String) -> Unit,
    override val onCategoryChange: (PostCategory) -> Unit,
    override val onSave: suspend () -> Result<Unit>
) : BasePostEditUiState
