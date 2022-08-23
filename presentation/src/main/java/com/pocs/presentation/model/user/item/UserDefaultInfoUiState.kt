package com.pocs.presentation.model.user.item

import java.io.Serializable

data class UserDefaultInfoUiState(
    val name: String,
    val email: String,
    val studentId: Int,
    val company: String?,
    val generation: Int,
    val github: String?
) : Serializable