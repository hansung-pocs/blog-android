package com.pocs.presentation.model

import androidx.paging.PagingData

data class ArticleUiState(
    val articlePagingData : PagingData<PostItemUiState> = PagingData.empty()
)