package com.pocs.data.model.auth

import com.google.gson.annotations.SerializedName
import com.pocs.data.model.user.UserDto

data class SessionValidResponseData(
    @SerializedName("user") val user: UserDto
)