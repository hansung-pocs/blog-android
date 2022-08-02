package com.pocs.data.mapper

import com.pocs.domain.model.user.UserType

fun String.toUserType() = UserType.valueOf(this.uppercase())

fun UserType.toDto() = this.toString().lowercase()