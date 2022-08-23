package com.pocs.domain.model.post

import com.pocs.domain.model.user.UserType

data class PostWriter(
    val id: Int,
    val name: String?,
    val email: String?,
    val type: UserType
)
