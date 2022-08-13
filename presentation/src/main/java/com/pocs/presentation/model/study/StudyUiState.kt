package com.pocs.presentation.model.study

import androidx.paging.PagingData
import com.pocs.presentation.model.post.item.PostItemUiState

data class StudyUiState(
    val pagingData: PagingData<PostItemUiState> = PagingData.empty(),
    val visiblePostWriteFab: Boolean
)