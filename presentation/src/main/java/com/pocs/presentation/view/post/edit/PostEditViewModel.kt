package com.pocs.presentation.view.post.edit

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.usecase.post.AddPostUseCase
import com.pocs.domain.usecase.post.UpdatePostUseCase
import com.pocs.presentation.model.post.PostEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor(
    private val updatePostUseCase : UpdatePostUseCase
) : ViewModel() {

    private lateinit var _uiState: MutableState<PostEditUiState>
    val uiState: State<PostEditUiState> get() = _uiState

    fun initUiState(id: Int, title: String, content: String, category: PostCategory) {
        assert(id > 0)
        _uiState = mutableStateOf(
            PostEditUiState(
                id = id,
                title = title,
                content = content,
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
        val result = updatePostUseCase(
            id = uiState.value.id,
            title = uiState.value.title,
            content = uiState.value.content,
            // TODO: 현재 접속중인 유저의 ID로 변경하기
            userId = 1,
            category = uiState.value.category
        )
        withContext(Dispatchers.IO) {
            delay(500)
        }
        if (result.isFailure) {
            _uiState.value = uiState.value.copy(isInSaving = false)
        }
        return result
    }
}