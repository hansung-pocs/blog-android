package com.pocs.data.mapper

import com.pocs.data.model.user.UserDto
import com.pocs.domain.model.user.User

fun UserDto.toEntity() = User(
    id = id,
    name = name,
    studentId = studentId,
    generation = generation
)