package com.pocs.presentation.view.post.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.domain.usecase.post.AddPostUseCase
import com.pocs.presentation.model.post.PostCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostCreateViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val isCurrentUserAdminUseCase: IsCurrentUserAdminUseCase
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
                isUserAdmin = isCurrentUserAdminUseCase(),
                onTitleChange = ::updateTitle,
                onContentChange = ::updateContent,
                onCategoryChange = ::updateCategory,
                onSave = ::savePost,
                showChips = category != PostCategory.QNA
            )
        )
    }

    private fun updateTitle(title: String) {
        _uiState!!.value = uiState.value.copy(title = title)
    }

    private fun updateContent(content: String) {
        _uiState!!.value = uiState.value.copy(content = content)
    }

    private fun updateCategory(category: PostCategory) {
        _uiState!!.value = uiState.value.copy(category = category)
    }

    private suspend fun savePost(): Result<Unit> {
        _uiState!!.value = uiState.value.copy(isInSaving = true)
        val result = addPostUseCase(
            title = uiState.value.title,
            content = uiState.value.content,
            category = uiState.value.category
        )
        _uiState!!.value = uiState.value.copy(isInSaving = false)
        return result
    }
}