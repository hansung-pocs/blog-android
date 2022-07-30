package com.pocs.presentation.model

import com.pocs.domain.model.user.User

sealed class UserDetailUiState {
    class Success(val user: User) : UserDetailUiState()
    class Failure(val e: Throwable, val onRetryClick: () -> Unit) : UserDetailUiState()
    object Loading : UserDetailUiState()
}
