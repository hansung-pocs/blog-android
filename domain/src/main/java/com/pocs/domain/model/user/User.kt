package com.pocs.domain.model.user

data class User(
    val id: Int,
    val defaultInfo: UserDefaultInfo? = null,
    val canceledAt: String? = null
)
