package com.pocs.presentation.model.user.anonymous

import com.pocs.presentation.model.admin.UserCreateInfoUiState

data class AnonymousCreateUiState(
    val createInfo: UserCreateInfoUiState = UserCreateInfoUiState(),
    val isInSaving: Boolean = false,
    val onSave: () -> Unit,
    val isSuccessToSave: Boolean = false,
    val errorMessage: String? = null,
    val shownErrorMessage: () -> Unit,
    private val onUpdateCreateInfo: (UserCreateInfoUiState) -> Unit,
){
    val canSave
        get() : Boolean {
            return createInfo.userName.isNotEmpty()
                    && createInfo.password.isNotEmpty()
        }

    fun updateCreateInfo(updater: (UserCreateInfoUiState) -> UserCreateInfoUiState) {
        onUpdateCreateInfo(updater(createInfo))
    }
}