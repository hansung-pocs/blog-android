package com.pocs.data.mapper

import com.pocs.domain.model.UserType

fun String.toUserType() = UserType.valueOf(this.uppercase())