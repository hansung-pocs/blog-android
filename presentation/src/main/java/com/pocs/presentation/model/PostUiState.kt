package com.pocs.presentation.model

import androidx.paging.PagingData

data class PostUiState(
    val articlePagingData : PagingData<PostItemUiState> = PagingData.empty()
)