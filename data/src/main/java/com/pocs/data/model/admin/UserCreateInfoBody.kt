package com.pocs.data.model.admin

data class UserCreateInfoBody(
    val userName: String,
    val password: String,
    val name: String,
    val studentId: Int,
    val email: String,
    val generation: Int,
    val type: String,
    val company: String,
    val github: String
)
