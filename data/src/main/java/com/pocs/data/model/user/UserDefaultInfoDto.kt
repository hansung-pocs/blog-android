package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

data class UserDefaultInfoDto(
    val name: String,
    val email: String,
    val studentId: Int,
    val company: String? = null,
    val generation: Int,
    val github: String? = null,
    @SerializedName("userProfilePath") val profilePath: String? = null
)