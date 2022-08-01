package com.pocs.data.model.admin

import com.google.gson.annotations.SerializedName

data class UserCreateInfoBody(
    @SerializedName("userName") val nickname: String,
    val password: String,
    val name: String,
    val studentId: Int,
    val email: String,
    val generation: Int,
    val type: String,
    val company: String,
    val github: String
)
