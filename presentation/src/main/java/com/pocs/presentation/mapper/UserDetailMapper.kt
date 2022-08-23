package com.pocs.presentation.mapper

import com.pocs.domain.model.user.UserDetail
import com.pocs.presentation.model.user.item.UserDetailItemUiState

fun UserDetail.toUiState() = UserDetailItemUiState(
    id = id,
    defaultInfo = defaultInfo?.toUiState(),
    type = type,
    createdAt = createdAt,
    canceledAt = canceledAt
)