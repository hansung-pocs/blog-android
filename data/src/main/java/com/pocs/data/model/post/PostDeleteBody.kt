package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostDeleteBody(
    @SerializedName("userId") val userId: Int
)