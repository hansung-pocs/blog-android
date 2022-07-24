package com.pocs.presentation.view.post.edit

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.PostCategory
import com.pocs.presentation.model.PostEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor() : ViewModel() {

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
                onChangeTitle = ::updateTitle,
                onChangeContent = ::updateContent,
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

    private suspend fun savePost(): Result<Boolean> {
        _uiState.value = uiState.value.copy(isInSaving = true)
        // TODO: API 연결하여야 함
        withContext(Dispatchers.IO) {
            delay(500)
        }
        val result = Result.success(true)
        if (result.isFailure) {
            _uiState.value = uiState.value.copy(isInSaving = false)
        }
        return result
    }
}