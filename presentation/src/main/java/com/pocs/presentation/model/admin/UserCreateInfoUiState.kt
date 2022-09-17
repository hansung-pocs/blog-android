package com.pocs.presentation.model.admin

import com.pocs.domain.model.user.UserType

data class UserCreateInfoUiState(
    val userName: String = "",
    val password: String = "",
    val name: String = "",
    val studentId: String = "",
    val email: String = "",
    val generation: String = "",
    val type: UserType = UserType.MEMBER,
    val company: String = "",
    val github: String = ""
)
