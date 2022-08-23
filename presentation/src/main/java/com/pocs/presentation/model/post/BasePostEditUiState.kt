package com.pocs.presentation.model.post

import androidx.compose.ui.text.input.TextFieldValue
import com.pocs.domain.model.post.PostCategory

interface BasePostEditUiState {
    val title: String
    val content: TextFieldValue
    val category: PostCategory
    val isUserAdmin: Boolean
    val isInSaving: Boolean
    val onTitleChange: (String) -> Unit
    val onContentChange: (TextFieldValue) -> Unit
    val onCategoryChange: (PostCategory) -> Unit
    val onSave: suspend () -> Result<Unit>

    val showChips get() = category != PostCategory.QNA
    val canSave get() = title.isNotEmpty() && content.text.isNotEmpty()
    val isEmpty get() = title.isEmpty() && content.text.isEmpty()
}
