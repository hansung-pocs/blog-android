package com.pocs.presentation.model.post

import androidx.paging.PagingData
import com.pocs.presentation.model.post.item.PostItemUiState

data class PostByUserUiState(
    val postPagingData: PagingData<PostItemUiState> = PagingData.empty()
)