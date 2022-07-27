package com.pocs.presentation.model

import com.pocs.domain.model.UserType

data class UserDetailItemUiState(
    val id: Int,
    val name: String,
    val email: String,
    val studentId: Int,
    val type: UserType,
    val company: String,
    val generation: Int,
    val github: String,
    val createdAt: String
)
