package com.pocs.presentation.model.user

import com.pocs.presentation.extension.canSaveAsGithubUrl
import com.pocs.presentation.extension.isValidEmail

data class UserEditUiState(
    val id: Int,
    val name: String,
    val email: String,
    val company: String?,
    val github: String?,
    val isInSaving: Boolean = false,
    private val onUpdate: (UserEditUiState) -> Unit,
    val onSave: suspend (password: String) -> Result<Unit>
) {
    val canSave: Boolean
        get() {
            return canSaveName && canSaveEmail && canSaveGithubUrl
        }

    val canSaveName = name.isNotEmpty()
    val canSaveEmail = email.isValidEmail()
    val canSaveGithubUrl = github == null || github.canSaveAsGithubUrl()

    fun update(updater: (UserEditUiState) -> UserEditUiState) {
        onUpdate(updater(this))
    }
}
