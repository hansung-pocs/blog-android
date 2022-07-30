package com.pocs.presentation.model.user

import androidx.paging.PagingData

data class UserUiState(
    val userPagingData: PagingData<UserItemUiState> = PagingData.empty()
)