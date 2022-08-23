package com.pocs.presentation.model.user.anonymous

data class AnonymousCreateUiState(
    val createInfo: AnonymousCreateInfoUiState = AnonymousCreateInfoUiState(),
    val isInCreating: Boolean = false,
    val onCreate: () -> Unit,
    val isSuccessToCreate: Boolean = false,
    val errorMessage: String? = null,
    val shownErrorMessage: () -> Unit,
    private val onUpdateCreateInfo: (AnonymousCreateInfoUiState) -> Unit,
){
    fun updateCreateInfo(updater: (AnonymousCreateInfoUiState) -> AnonymousCreateInfoUiState) {
        onUpdateCreateInfo(updater(createInfo))
    }

    val enableCreateButton: Boolean get() = createInfo.userName.isNotEmpty() && createInfo.password.isNotEmpty()
}