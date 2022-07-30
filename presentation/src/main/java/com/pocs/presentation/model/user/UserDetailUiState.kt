package com.pocs.presentation.model.user

import com.pocs.presentation.model.user.item.UserDetailItemUiState

sealed class UserDetailUiState {
    class Success(val userDetail: UserDetailItemUiState) : UserDetailUiState()
    class Failure(val e: Throwable, val onRetryClick: () -> Unit) : UserDetailUiState()
    object Loading : UserDetailUiState()
}
