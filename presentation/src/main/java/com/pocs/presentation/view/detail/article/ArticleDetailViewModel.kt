package com.pocs.presentation.view.detail.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.presentation.mock.mockArticleDetailItems
import com.pocs.presentation.model.ArticleDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ArticleDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun loadArticle(id: Int) {
        viewModelScope.launch {
            val article = mockArticleDetailItems.first { it.id == id }
            _uiState.update { it.copy(article = article) }
        }
    }
}