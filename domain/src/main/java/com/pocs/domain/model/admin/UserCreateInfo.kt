package com.pocs.domain.model.admin

import com.pocs.domain.model.user.UserType

data class UserCreateInfo(
    val userName: String,
    val password: String,
    val name: String,
    val studentId: Int,
    val email: String,
    val generation: Int,
    val type: UserType,
    val company: String,
    val github: String
)
