package com.pocs.presentation.model

data class UserEditUiState(
    val name: String,
    val email: String,
    val studentId: String,
    val company: String,
    val github: String,
    val isInSaving: Boolean = false,
    val onSave: suspend () -> Result<Unit>
)
