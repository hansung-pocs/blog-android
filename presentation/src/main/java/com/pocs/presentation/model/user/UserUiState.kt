package com.pocs.presentation.model.user

import androidx.paging.PagingData
import com.pocs.presentation.model.user.item.UserItemUiState

data class UserUiState(
    val userPagingData: PagingData<UserItemUiState> = PagingData.empty()
)