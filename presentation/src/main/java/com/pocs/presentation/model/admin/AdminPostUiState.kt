package com.pocs.presentation.model.admin

import androidx.paging.PagingData
import com.pocs.presentation.model.post.item.PostItemUiState

data class AdminPostUiState(
    val postPagingData: PagingData<PostItemUiState> = PagingData.empty()
)
