package com.pocs.presentation.model

import androidx.paging.PagingData

data class UserListUiState(
    val userPagingData: PagingData<UserItemUiState> = PagingData.empty()
)