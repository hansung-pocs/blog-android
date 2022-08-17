package com.pocs.data.mapper

import com.pocs.data.model.nonmember.NonMemberCreateInfoBody
import com.pocs.domain.model.nonmember.NonMemberCreateInfo

fun NonMemberCreateInfo.toDto() = NonMemberCreateInfoBody(
    userName = userName,
    password = password
)