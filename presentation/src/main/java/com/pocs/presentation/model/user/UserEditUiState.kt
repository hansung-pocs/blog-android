package com.pocs.presentation.model.user

data class UserEditUiState(
    val id: Int,
    val name: String,
    val email: String,
    val company: String,
    val github: String,
    val isInSaving: Boolean = false,
    private val onUpdate: (UserEditUiState) -> Unit,
    val onSave: suspend (password: String) -> Result<Unit>
) {
    fun update(updater: (UserEditUiState) -> UserEditUiState) {
        onUpdate(updater(this))
    }
}
