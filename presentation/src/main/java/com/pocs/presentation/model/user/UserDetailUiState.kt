package com.pocs.presentation.model.user

import com.pocs.domain.model.user.UserDetail

sealed class UserDetailUiState {
    class Success(val userDetail: UserDetail) : UserDetailUiState()
    class Failure(val e: Throwable, val onRetryClick: () -> Unit) : UserDetailUiState()
    object Loading : UserDetailUiState()
}
