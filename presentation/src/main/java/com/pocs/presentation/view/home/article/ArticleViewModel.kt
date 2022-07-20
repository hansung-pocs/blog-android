package com.pocs.presentation.view.home.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.presentation.mock.mockArticleItemsPagingData
import com.pocs.presentation.model.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(articlePagingData = mockArticleItemsPagingData)
            }
        }
    }
}