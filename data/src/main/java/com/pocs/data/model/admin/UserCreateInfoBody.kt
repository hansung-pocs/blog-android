package com.pocs.data.model.admin

import com.google.gson.annotations.SerializedName

data class UserCreateInfoBody(
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("studentId") val studentId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("generation") val generation: Int,
    @SerializedName("type") val type: String,
    @SerializedName("company") val company: String,
    @SerializedName("github") val github: String
)
