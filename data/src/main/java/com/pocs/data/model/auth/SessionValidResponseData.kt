package com.pocs.data.model.auth

import com.pocs.data.model.user.UserDto

data class SessionValidResponseData(
    val user: UserDto
)