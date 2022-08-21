package com.pocs.presentation.model.post

import androidx.compose.ui.text.input.TextFieldValue
import com.pocs.domain.model.post.PostCategory

data class PostCreateUiState(
    override val title: String = "",
    override val content: TextFieldValue = TextFieldValue(),
    override val category: PostCategory,
    override val isUserAdmin: Boolean,
    override val isInSaving: Boolean = false,
    override val onTitleChange: (String) -> Unit,
    override val onContentChange: (TextFieldValue) -> Unit,
    override val onCategoryChange: (PostCategory) -> Unit,
    override val onSave: suspend () -> Result<Unit>
) : BasePostEditUiState
