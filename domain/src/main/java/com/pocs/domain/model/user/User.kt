package com.pocs.domain.model.user

data class User(
    val id: Int,
    val name: String,
    val studentId: Int,
    val generation: Int,
    val canceledAt: String
)
