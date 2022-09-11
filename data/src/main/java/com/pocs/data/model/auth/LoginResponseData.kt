package com.pocs.data.model.auth

import com.google.gson.annotations.SerializedName
import com.pocs.data.model.user.UserDto

data class LoginResponseData(
    @SerializedName("sessionToken") val sessionToken: String,
    @SerializedName("user") val user: UserDto
)
