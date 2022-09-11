package com.pocs.data.model.auth

import com.pocs.data.model.user.UserDto

data class LoginResponseData(
    val sessionToken: String,
    val user: UserDto
)
