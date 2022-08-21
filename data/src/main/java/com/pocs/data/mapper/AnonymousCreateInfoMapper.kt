package com.pocs.data.mapper

import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
import com.pocs.domain.model.user.AnonymousCreateInfo

fun AnonymousCreateInfo.toDto() = AnonymousCreateInfoBody(
    userName = userName,
    password = password
)