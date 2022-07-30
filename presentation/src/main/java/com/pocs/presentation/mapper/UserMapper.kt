package com.pocs.presentation.mapper

import com.pocs.domain.model.user.User
import com.pocs.presentation.model.user.item.UserItemUiState

fun User.toUiState() = UserItemUiState(
    id = id,
    name = name,
    studentId = studentId.toString(),
    generation = generation
)