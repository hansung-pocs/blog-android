package com.pocs.presentation.model

import androidx.paging.PagingData

data class UserUiState(
    val userPagingData: PagingData<UserItemUiState> = PagingData.empty()
)