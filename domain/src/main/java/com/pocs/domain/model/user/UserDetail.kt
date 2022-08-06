package com.pocs.domain.model.user

data class UserDetail(
    val id: Int,
    val name: String,
    val email: String,
    val studentId: Int,
    val type: UserType,
    val company: String? = null,
    val generation: Int,
    val github: String? = null,
    val createdAt: String,
    val canceledAt: String? = null,
)
