package com.pocs.presentation.mapper

import com.pocs.domain.model.admin.UserCreateInfo
import com.pocs.presentation.model.admin.UserCreateInfoUiState

fun UserCreateInfoUiState.toEntity() = UserCreateInfo(
    nickname = nickname,
    password = password,
    name = name,
    studentId = studentId.toInt(),
    email = email,
    generation = generation.toInt(),
    type = type,
    company = company,
    github = github
)