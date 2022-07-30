package com.pocs.domain.model.user

import java.io.Serializable

data class UserDetail(
    val id: Int,
    val name: String,
    val email: String,
    val studentId: Int,
    val type: UserType,
    val company: String,
    val generation: Int,
    val github: String,
    val createdAt: String
) : Serializable
