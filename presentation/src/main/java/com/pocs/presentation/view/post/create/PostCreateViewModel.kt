package com.pocs.presentation.view.post.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.usecase.post.AddPostUseCase
import com.pocs.presentation.model.PostCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostCreateViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase
) : ViewModel() {

    private lateinit var _uiState: MutableState<PostCreateUiState>
    val uiState: State<PostCreateUiState> get() = _uiState

    fun initUiState(category: PostCategory) {
        _uiState = mutableStateOf(
            PostCreateUiState(
                category = category,
                onTitleChange = ::updateTitle,
                onContentChange = ::updateContent,
                onSave = ::savePost
            )
        )
    }

    private fun updateTitle(title: String) {
        _uiState.value = uiState.value.copy(title = title)
    }

    private fun updateContent(content: String) {
        _uiState.value = uiState.value.copy(content = content)
    }

    private suspend fun savePost(): Result<Unit> {
        _uiState.value = uiState.value.copy(isInSaving = true)
        val result = addPostUseCase(
            title = uiState.value.title,
            content = uiState.value.content,
            // TODO: 현재 접속중인 유저의 ID로 변경하기
            userId = 1,
            category = uiState.value.category
        )
        _uiState.value = uiState.value.copy(isInSaving = false)
        return result
    }
}