package com.pocs.presentation.model.auth

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

    val enableLoginButton: Boolean get() = userName.isNotEmpty() && password.isNotEmpty()
}