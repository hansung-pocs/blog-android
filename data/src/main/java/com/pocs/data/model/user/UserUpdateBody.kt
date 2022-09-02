package com.pocs.data.model.user

data class UserUpdateBody(
    val password: String?,
    val name: String,
    val email: String,
    val github: String?,
    val company: String?,
    val profileImageUrl: String?
)
