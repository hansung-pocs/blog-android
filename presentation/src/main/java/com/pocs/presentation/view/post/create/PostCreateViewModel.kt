package com.pocs.presentation.view.post.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.post.AddPostUseCase
import com.pocs.presentation.model.post.PostCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostCreateViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val getCurrentUserTypeUseCase: GetCurrentUserTypeUseCase
) : ViewModel() {

    private var _uiState: MutableState<PostCreateUiState>? = null
    val uiState: State<PostCreateUiState> get() = requireNotNull(_uiState)

    fun initUiState(category: PostCategory) {
        if (_uiState != null) {
            return
        }
        _uiState = mutableStateOf(
            PostCreateUiState(
                category = category,
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
        val result = addPostUseCase(
            title = uiState.value.title,
            content = uiState.value.content.text,
            category = uiState.value.category,
            onlyMember = uiState.value.onlyMember
        )
        _uiState!!.value = uiState.value.copy(isInSaving = false)
        return result
    }
}