package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userId") val id: Int,
    @SerializedName("userName") val name: String,
    val email: String,
    val studentId: Int,
    val type: String,
    val company: String,
    val generation: Int,
    val github: String,
    val createdAt: String
)
