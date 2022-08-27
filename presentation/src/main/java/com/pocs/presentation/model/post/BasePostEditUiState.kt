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
    val isInSaving: Boolean
    val onTitleChange: (String) -> Unit
    val onContentChange: (TextFieldValue) -> Unit
    val onCategoryChange: (PostCategory) -> Unit
    val onOnlyMemberChange: (Boolean) -> Unit
    val onSave: suspend () -> Result<Unit>

    val showChips get() = category != PostCategory.QNA
    val isUserAdmin get() = currentUserType == UserType.ADMIN
    val showOnlyMemberButton get() = currentUserType != UserType.ANONYMOUS
    val canSave get() = title.isNotEmpty() && content.text.isNotEmpty()
    val isEmpty get() = title.isEmpty() && content.text.isEmpty()
}
