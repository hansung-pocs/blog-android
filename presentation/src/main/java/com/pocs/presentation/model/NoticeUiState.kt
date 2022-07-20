package com.pocs.presentation.model

import androidx.paging.PagingData

data class NoticeUiState(
    val noticePagingData : PagingData<PostItemUiState> = PagingData.empty()
)