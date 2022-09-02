package com.pocs.domain.model.user

data class UserDefaultInfo(
    val name: String,
    val email: String,
    val studentId: Int,
    val company: String? = null,
    val generation: Int,
    val github: String? = null,
    val profileImageUrl: String? = null
)