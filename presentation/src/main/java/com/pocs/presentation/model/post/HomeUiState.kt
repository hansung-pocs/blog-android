package com.pocs.presentation.model.post

data class HomeUiState(
    val isCurrentUserAdmin: Boolean,
    val userMessage: String? = null
)