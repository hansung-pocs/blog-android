package com.pocs.presentation.mapper

import com.pocs.domain.model.user.UserDefaultInfo
import com.pocs.presentation.model.user.item.UserDefaultInfoUiState

fun UserDefaultInfo.toUiState() = UserDefaultInfoUiState(
    name = name,
    email = email,
    // TODO: API 구현되어 domain 모듈 모델들 수정되면 수정하기
    profileImageUrl = null,
    studentId = studentId,
    company = company,
    generation = generation,
    github = github,
    profileImageUrl = profileImageUrl
)