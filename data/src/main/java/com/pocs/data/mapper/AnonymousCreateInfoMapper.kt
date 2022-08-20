package com.pocs.data.mapper

import com.pocs.data.model.user.anonymous.AnonymousCreateInfoBody
import com.pocs.domain.model.nonmember.NonMemberCreateInfo

fun NonMemberCreateInfo.toDto() = AnonymousCreateInfoBody(
    userName = userName,
    password = password
)