package com.pocs.data.mapper

import com.pocs.data.model.user.UserListSortingMethodDto
import com.pocs.domain.model.user.UserListSortingMethod

fun UserListSortingMethod.toDto(): UserListSortingMethodDto? {
    // API에서 널인경우 가입일 내림차순으로 처리하고 있다.
    return if (this == UserListSortingMethod.CREATED_AT) {
        null
    } else {
        UserListSortingMethodDto.valueOf(this.toString())
    }
}
