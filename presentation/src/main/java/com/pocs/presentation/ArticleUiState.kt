package com.pocs.presentation

import androidx.paging.PagingData

data class ArticleUiState(
    val articlePagingData : PagingData<ArticleItemUiState> = PagingData.empty()
)