package com.pocs.presentation.model.post

import androidx.compose.ui.text.input.TextFieldValue
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.user.UserType

data class PostEditUiState(
    val postId: Int,
    override val title: String,
    override val content: TextFieldValue,
    override val category: PostCategory,
    override val currentUserType: UserType,
    override val onlyMember: Boolean,
    override val isUserAnonymous: Boolean = currentUserType == UserType.ANONYMOUS,
    override val isInSaving: Boolean = false,
    override val onTitleChange: (String) -> Unit,
    override val onContentChange: (TextFieldValue) -> Unit,
    override val onCategoryChange: (PostCategory) -> Unit,
    override val onOnlyMemberChange: (Boolean) -> Unit,
    override val onSave: suspend () -> Result<Unit>
) : BasePostEditUiState
