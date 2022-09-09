package com.pocs.presentation.model.auth

import com.pocs.presentation.constant.MIN_USER_PASSWORD_LEN

data class LoginUiState(
    val hideSplashScreen: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userMessage: String? = null,
    val userName: String = "",
    val password: String = "",
    private val onUpdate: (LoginUiState) -> Unit
) {
    fun update(function: (LoginUiState) -> LoginUiState) {
        onUpdate(function(this))
    }

    val enableLoginButton: Boolean
        get() = userName.isNotEmpty() && password.length >= MIN_USER_PASSWORD_LEN
}