package com.pocs.domain.model

data class User(
    val id: Int,
    val userName: String,
    val email: String,
    val studentId: Int,
    val type: UserType,
    val company: String,
    val generation: Int,
    val github: String,
    val createdAt: Int
)
