package com.pocs.data.mapper

import com.pocs.data.model.user.UserListSortingMethodDto
import com.pocs.domain.model.user.UserListSortingMethod

fun UserListSortingMethod.toDto() = UserListSortingMethodDto.valueOf(this.toString())