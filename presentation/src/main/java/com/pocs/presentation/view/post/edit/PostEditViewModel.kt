package com.pocs.presentation.view.post.edit

import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.post.UpdatePostUseCase
import com.pocs.presentation.model.post.PostEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor(
    private val updatePostUseCase: UpdatePostUseCase,
    private val getCurrentUserTypeUseCase: GetCurrentUserTypeUseCase
) : ViewModel() {

    private var _uiState: MutableState<PostEditUiState>? = null
    val uiState: State<PostEditUiState> get() = requireNotNull(_uiState)

    fun initUiState(
        id: Int,
        title: String,
        content: String,
        category: PostCategory,
        onlyMember: Boolean
    ) {
        assert(id > 0)
        if (_uiState != null) {
            return
        }
        _uiState = mutableStateOf(
            PostEditUiState(
                postId = id,
                title = title,
                content = TextFieldValue(content),
                category = category,
                onlyMember = onlyMember,
                currentUserType = getCurrentUserTypeUseCase(),
                onTitleChange = ::updateTitle,
                onContentChange = ::updateContent,
                onCategoryChange = ::updateCategory,
                onSave = ::savePost,
                onOnlyMemberChange = ::updateOnlyMember
            )
        )
    }

    private fun updateTitle(title: String) {
        _uiState!!.value = uiState.value.copy(title = title)
    }

    private fun updateContent(content: TextFieldValue) {
        _uiState!!.value = uiState.value.copy(content = content)
    }

    private fun updateCategory(category: PostCategory) {
        _uiState!!.value = uiState.value.copy(category = category)
    }

    private fun updateOnlyMember(onlyMember: Boolean) {
        _uiState!!.value = uiState.value.copy(onlyMember = onlyMember)
    }

    private suspend fun savePost(): Result<Unit> {
        _uiState!!.value = uiState.value.copy(isInSaving = true)
        val result = updatePostUseCase(
            id = uiState.value.postId,
            title = uiState.value.title,
            content = uiState.value.content.text,
            category = uiState.value.category,
            onlyMember = uiState.value.onlyMember
        )
        if (result.isFailure) {
            _uiState!!.value = uiState.value.copy(isInSaving = false)
        }
        return result
    }
}
