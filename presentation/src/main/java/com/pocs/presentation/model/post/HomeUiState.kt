package com.pocs.presentation.model.post

data class HomeUiState(
    val isCurrentUserAdmin: Boolean,
    val replaceToLoginActivity: Boolean = false,
    val hideSplashScreen: Boolean = false
)