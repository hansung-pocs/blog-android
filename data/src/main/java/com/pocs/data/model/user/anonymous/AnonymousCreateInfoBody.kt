package com.pocs.data.model.user.anonymous

import com.google.gson.annotations.SerializedName

data class AnonymousCreateInfoBody(
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String
)