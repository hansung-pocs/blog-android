package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userId") val id: Int,
    val name: String,
    val email: String,
    val studentId: Int,
    val type: String,
    val company: String? = null,
    val generation: Int,
    val github: String? = null,
    val createdAt: String,
    val canceledAt: String? = null
)
