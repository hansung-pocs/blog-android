package com.pocs.presentation.mapper

import com.pocs.domain.model.user.UserDefaultInfo
import com.pocs.presentation.model.user.item.UserDefaultInfoUiState

fun UserDefaultInfo.toUiState() = UserDefaultInfoUiState(
    name = name,
    email = email,
    profileImageUrl = profileImageUrl,
    studentId = studentId,
    company = company,
    generation = generation,
    github = github
)