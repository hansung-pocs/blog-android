package com.pocs.presentation.view.post.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var uiState by mutableStateOf(
        PostEditUiState(
            onChangeTitle = ::updateTitle,
            onChangeContent = ::updateContent,
            onSave = ::savePost
        )
    )
        private set

    fun initUiState(id: Int, title: String, content: String, category: PostCategory) {
        uiState = uiState.copy(id = id, title = title, content = content, category = category)
    }

    private fun updateTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    private fun updateContent(content: String) {
        uiState = uiState.copy(content = content)
    }

    private suspend fun savePost(): Result<Boolean> {
        // TODO: API 연결하여야 함
        withContext(Dispatchers.IO) {
            delay(500)
        }
        return Result.success(true)
    }
}