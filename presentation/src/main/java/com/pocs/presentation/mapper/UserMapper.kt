package com.pocs.presentation.mapper

import com.pocs.domain.model.User
import com.pocs.presentation.model.UserDetailItemUiState

fun User.toDetailItemUiState() = UserDetailItemUiState(
    id = id,
    name = name,
    email = email,
    studentId = studentId,
    type = type,
    company = company,
    generation = generation,
    github = github,
    createdAt = createdAt
)