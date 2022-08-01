package com.pocs.presentation.mapper

import com.pocs.domain.model.user.UserDetail
import com.pocs.presentation.model.user.item.UserDetailItemUiState

fun UserDetail.toUiState() = UserDetailItemUiState(
    id = id,
    name = name,
    email = email,
    studentId = studentId,
    type = type,
    company = company,
    generation = generation,
    github = github,
    createdAt = createdAt,
    canceledAt = canceledAt
)