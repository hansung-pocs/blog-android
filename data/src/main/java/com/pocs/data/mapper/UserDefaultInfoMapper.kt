package com.pocs.data.mapper

import com.pocs.data.BuildConfig
import com.pocs.data.model.user.UserDefaultInfoDto
import com.pocs.domain.model.user.UserDefaultInfo

fun UserDefaultInfoDto.toEntity() = UserDefaultInfo(
    name = name,
    email = email,
    studentId = studentId,
    company = company,
    generation = generation,
    github = github,
    profileImageUrl = profilePath?.toUserProfileImageUrl()
)

fun String.toUserProfileImageUrl(): String {
    return this.let { BuildConfig.IMAGE_URL_PREFIX + it + ".png" }
}