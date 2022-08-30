package com.pocs.presentation.model.post

import androidx.compose.ui.text.input.TextFieldValue
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.user.UserType

interface BasePostEditUiState {
    val title: String
    val content: TextFieldValue
    val category: PostCategory
    val currentUserType: UserType
    val onlyMember: Boolean
    val isUserAnonymous : Boolean
    val isInSaving: Boolean
    val onTitleChange: (String) -> Unit
    val onContentChange: (TextFieldValue) -> Unit
    val onCategoryChange: (PostCategory) -> Unit
    val onOnlyMemberChange: (Boolean) -> Unit
    val onSave: suspend () -> Result<Unit>

    val showChips get() = currentUserType != UserType.ANONYMOUS
    val showOnlyMemberButton get() = currentUserType != UserType.ANONYMOUS
    val isUserAdmin get() = currentUserType == UserType.ADMIN
    val canSave get() = title.isNotEmpty() && content.text.isNotEmpty()
    val isEmpty get() = title.isEmpty() && content.text.isEmpty()
}
