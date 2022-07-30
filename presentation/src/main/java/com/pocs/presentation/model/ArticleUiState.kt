package com.pocs.presentation.model

import androidx.paging.PagingData
import com.pocs.presentation.model.post.PostItemUiState

data class ArticleUiState(
    val articlePagingData : PagingData<PostItemUiState> = PagingData.empty()
)