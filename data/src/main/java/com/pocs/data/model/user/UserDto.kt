package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userId") val id: Int,
    val defaultInfo: UserDefaultInfoDto? = null,
    val type: String,
    val createdAt: String,
    val canceledAt: String? = null
)
