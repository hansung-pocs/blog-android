package com.pocs.presentation.model.post

import androidx.paging.PagingData
import com.pocs.presentation.model.post.item.PostItemUiState

data class PostUiState(
    val pagingData : PagingData<PostItemUiState> = PagingData.empty(),
    val visiblePostWriteFab: Boolean
)