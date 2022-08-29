package com.pocs.presentation.model.user

import com.pocs.presentation.model.user.item.UserDetailItemUiState

sealed class UserDetailUiState {
    data class Success(
        val userDetail: UserDetailItemUiState,
        val isCurrentUserAdmin: Boolean,
        val isMyInfo: Boolean,
        val errorMessage: String? = null,
        val shownErrorMessage: () -> Unit,
        val onKickClick: () -> Unit,
        val successToKick: Boolean
    ) : UserDetailUiState()

    class Failure(val e: Throwable, val onRetryClick: () -> Unit) : UserDetailUiState()
    object Loading : UserDetailUiState()
}
