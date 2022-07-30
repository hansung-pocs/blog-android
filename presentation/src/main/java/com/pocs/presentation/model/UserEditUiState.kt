package com.pocs.presentation.model

data class UserEditUiState(
    val name: String,
    val studentId: String,
    val email: String,
    val company: String,
    val github: String,
    val isInSaving: Boolean = false,
    private val onUpdate: (UserEditUiState) -> Unit,
    val onSave: suspend () -> Result<Unit>
) {
    fun update(updater: (UserEditUiState) -> UserEditUiState) {
        onUpdate(updater(this))
    }
}
