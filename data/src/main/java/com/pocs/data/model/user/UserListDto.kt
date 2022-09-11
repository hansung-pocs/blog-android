package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

data class UserListDto(
    @SerializedName("users") val users: List<UserDto>
)
