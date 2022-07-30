package com.pocs.presentation.model

import androidx.paging.PagingData
import com.pocs.presentation.model.post.PostItemUiState

data class NoticeUiState(
    val noticePagingData : PagingData<PostItemUiState> = PagingData.empty()
)