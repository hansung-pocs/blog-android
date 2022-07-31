package com.pocs.presentation.model.admin

import androidx.paging.PagingData
import com.pocs.presentation.model.user.item.UserItemUiState

data class AdminUserUiState(
    val userPagingData: PagingData<UserItemUiState> = PagingData.empty()
)
