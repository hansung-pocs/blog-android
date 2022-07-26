package com.pocs.presentation.view.post.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.PostCategory
import com.pocs.presentation.model.PostCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostCreateViewModel @Inject constructor() : ViewModel() {

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