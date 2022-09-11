package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

data class UserUpdateBody(
    @SerializedName("password") val password: String?,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("github") val github: String?,
    @SerializedName("company") val company: String?
)
