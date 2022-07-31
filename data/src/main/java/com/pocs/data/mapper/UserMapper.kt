package com.pocs.data.mapper

import com.pocs.data.model.admin.AdminUserDto
import com.pocs.data.model.user.UserDto
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail

fun UserDto.toEntity() = User(
    id = id,
    name = name,
    studentId = studentId,
    generation = generation,
    canceledAt = "-"
)

fun UserDto.toDetailEntity() = UserDetail(
    id = id,
    name = name,
    email = email,
    studentId = studentId,
    type = type.toUserType(),
    company = company,
    generation = generation,
    github = github,
    createdAt = createdAt,
    canceledAt = "-"
)

fun AdminUserDto.toEntity() = User(
    id = userId,
    name = userName,
    studentId = studentId,
    generation = generation,
    canceledAt = canceledAt
)