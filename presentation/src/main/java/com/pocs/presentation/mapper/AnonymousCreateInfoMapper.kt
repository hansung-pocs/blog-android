package com.pocs.presentation.mapper

import com.pocs.domain.model.user.AnonymousCreateInfo
import com.pocs.presentation.model.user.anonymous.AnonymousCreateInfoUiState

fun AnonymousCreateInfoUiState.toEntity() = AnonymousCreateInfo(
    userName = userName,
    password = password
)