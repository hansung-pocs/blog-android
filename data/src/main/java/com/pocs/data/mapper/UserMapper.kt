package com.pocs.data.mapper

import com.pocs.data.model.user.UserDto
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserDetail

fun UserDto.toEntity() = User(
    id = id,
    defaultInfo = defaultInfo?.toEntity(),
    canceledAt = canceledAt
)

fun UserDto.toDetailEntity() = UserDetail(
    id = id,
    type = type.toUserType(),
    createdAt = createdAt,
    canceledAt = canceledAt,
    defaultInfo = defaultInfo?.toEntity()
)
