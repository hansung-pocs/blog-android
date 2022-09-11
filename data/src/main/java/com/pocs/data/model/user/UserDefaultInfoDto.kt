package com.pocs.data.model.user

import com.google.gson.annotations.SerializedName

data class UserDefaultInfoDto(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("studentId") val studentId: Int,
    @SerializedName("company") val company: String? = null,
    @SerializedName("generation") val generation: Int,
    @SerializedName("github") val github: String? = null
)