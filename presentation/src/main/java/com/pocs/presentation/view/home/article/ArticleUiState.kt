package com.pocs.presentation.view.home.article

import androidx.paging.PagingData

data class ArticleUiState(
    val articlePagingData : PagingData<ArticleItemUiState> = PagingData.empty()
)