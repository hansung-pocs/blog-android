package com.pocs.data.model.admin

data class AdminUserDto(
    val userId: Int,
    val userName: String,
    val email: String,
    val studentId: Int,
    val type: String,
    val company: String,
    val generation: Int,
    val github: String,
    val createdAt: String,
    val canceledAt: String
)
