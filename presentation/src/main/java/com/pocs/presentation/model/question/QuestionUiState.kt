package com.pocs.presentation.model.question

import androidx.paging.PagingData
import com.pocs.presentation.model.post.item.PostItemUiState

data class QuestionUiState(
    val pagingData: PagingData<PostItemUiState> = PagingData.empty()
)
