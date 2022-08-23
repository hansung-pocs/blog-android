package com.pocs.domain.model.user

data class UserDetail(
    val id: Int,
    val defaultInfo: UserDefaultInfo?,
    val type: UserType,
    val createdAt: String,
    val canceledAt: String? = null
)
