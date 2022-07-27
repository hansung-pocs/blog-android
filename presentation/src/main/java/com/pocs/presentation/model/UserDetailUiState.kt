package com.pocs.presentation.model

sealed class UserDetailUiState {
    class Success(val userDetailItem: UserDetailItemUiState) : UserDetailUiState()
    class Failure(val e: Throwable) : UserDetailUiState()
    object Loading : UserDetailUiState()
}
