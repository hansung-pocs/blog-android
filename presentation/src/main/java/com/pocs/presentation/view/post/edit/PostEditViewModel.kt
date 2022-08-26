package com.pocs.presentation.view.post.edit

import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.domain.usecase.post.UpdatePostUseCase
import com.pocs.presentation.model.post.PostEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor(
    private val updatePostUseCase: UpdatePostUseCase,
    private val isCurrentUserAdminUseCase: IsCurrentUserAdminUseCase
) : ViewModel() {

    private var _uiState: MutableState<PostEditUiState>? = null
    val uiState: State<PostEditUiState> get() = requireNotNull(_uiState)

    fun initUiState(id: Int, title: String, content: String, category: PostCategory) {
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
                isUserAdmin = isCurrentUserAdminUseCase(),
                onTitleChange = ::updateTitle,
                onContentChange = ::updateContent,
                onCategoryChange = ::updateCategory,
                onSave = ::savePost,
                // TODO : 게시글 편집시에는 체크박스에 따라 편집이 가능하여야 함, 임시 변수 설정
                onlyMember = true
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

    private suspend fun savePost(): Result<Unit> {
        _uiState!!.value = uiState.value.copy(isInSaving = true)
        val result = updatePostUseCase(
            id = uiState.value.postId,
            title = uiState.value.title,
            content = uiState.value.content.text,
            category = uiState.value.category
        )
        if (result.isFailure) {
            _uiState!!.value = uiState.value.copy(isInSaving = false)
        }
        return result
    }
}