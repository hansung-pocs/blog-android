package com.pocs.presentation.model.admin

data class AdminUserCreateUiState(
    val createInfo: UserCreateInfoUiState = UserCreateInfoUiState(),
    val isInSaving: Boolean = false,
    val onSave: () -> Unit,
    val isSuccessToSave: Boolean = false,
    val errorMessage: String? = null,
    val shownErrorMessage: () -> Unit,
    private val onUpdateCreateInfo: (UserCreateInfoUiState) -> Unit,
) {
    val canSave
        get() : Boolean {
            return createInfo.nickname.isNotEmpty()
                    && createInfo.password.isNotEmpty()
                    && createInfo.name.isNotEmpty()
                    && createInfo.studentId.isNotEmpty()
                    && createInfo.email.isNotEmpty()
                    && createInfo.generation.isNotEmpty()
        }

    fun updateCreateInfo(updater: (UserCreateInfoUiState) -> UserCreateInfoUiState) {
        onUpdateCreateInfo(updater(createInfo))
    }
}
