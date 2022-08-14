package com.pocs.data.mapper

import com.pocs.data.model.admin.UserCreateInfoBody
import com.pocs.domain.model.admin.UserCreateInfo

fun UserCreateInfo.toDto() = UserCreateInfoBody(
    userName = userName,
    password = password,
    name = name,
    studentId = studentId,
    email = email,
    generation = generation,
    type = type.toDto(),
    company = company,
    github = github
)