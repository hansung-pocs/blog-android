package com.pocs.presentation.model.setting

import com.pocs.domain.model.user.UserDetail

data class SettingUiState(
    val currentUser: UserDetail? = null,
    val errorMessage: String? = null,
    val onSuccessToLogout: Boolean = false
)
