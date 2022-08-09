package com.pocs.presentation.model

import androidx.paging.PagingData
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
import com.pocs.presentation.model.post.item.PostItemUiState

data class ArticleUiState(
    val articlePagingData : PagingData<PostItemUiState> = PagingData.empty(),
    val visiblePostWriteFab: Boolean
)