package com.pocs.data.mapper

import com.pocs.data.model.UserDto
import com.pocs.domain.model.user.User

fun UserDto.toEntity() = User(
    id = id,
    name = name,
    email = email,
    studentId = -1,
    type = type.toUserType(),
    company = "",
    generation = -1,
    github = "",
    createdAt = "",
)