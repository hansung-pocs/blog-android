package com.pocs.presentation.model.setting

import com.pocs.presentation.model.user.item.UserDetailItemUiState

data class SettingUiState(
    val currentUser: UserDetailItemUiState,
    val errorMessage: String? = null,
    val onSuccessToLogout: Boolean = false
)
