package com.pocs.presentation.model.user

import android.graphics.Bitmap
import com.pocs.presentation.constant.MIN_USER_PASSWORD_LEN
import com.pocs.presentation.extension.canSaveAsGithubUrl
import com.pocs.presentation.extension.isValidEmail

data class UserEditUiState(
    val id: Int,
    val password: String = "",
    val name: String,
    val email: String,
    val profileImageUrl: String?,
    val newProfileImage: Bitmap? = null,
    val company: String?,
    val github: String?,
    val profileImageUrl: String?,
    val isInSaving: Boolean = false,
    val message: String? = null,
    private val onUpdate: (UserEditUiState) -> Unit,
    val onSave: suspend () -> Result<Unit>
) {
    val canSave: Boolean
        get() {
            return canSaveName && canSavePassword && canSaveEmail && canSaveGithubUrl
        }

    private val canSavePassword = password.isEmpty() || password.length >= MIN_USER_PASSWORD_LEN
    val canSaveName = name.isNotEmpty()
    val canSaveEmail = email.isValidEmail()
    val canSaveGithubUrl = github == null || github.canSaveAsGithubUrl()

    fun update(updater: (UserEditUiState) -> UserEditUiState) {
        onUpdate(updater(this))
    }
}
