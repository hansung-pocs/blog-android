package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("userId") val id: Int,
    @SerializedName("defaultInfo") val defaultInfo: UserDefaultInfoDto? = null,
    @SerializedName("type") val type: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("canceledAt") val canceledAt: String? = null
)
