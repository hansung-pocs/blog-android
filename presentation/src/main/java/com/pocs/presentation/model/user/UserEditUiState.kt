package com.pocs.presentation.model.user

import com.pocs.presentation.extension.isValidEmail
import com.pocs.presentation.extension.isValidGithubUserUrl

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
            return name.isNotEmpty() && isEmailValid && isGithubUrlValid
        }

    val isEmailValid = email.isValidEmail()
    val isGithubUrlValid = github == null || github.isEmpty() || github.isValidGithubUserUrl()

    fun update(updater: (UserEditUiState) -> UserEditUiState) {
        onUpdate(updater(this))
    }
}
