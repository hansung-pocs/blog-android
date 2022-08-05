package com.pocs.data.model

import com.google.gson.annotations.SerializedName

data class ResponseBody<T>(
    val message: String,
    val status: Int,
    @SerializedName("servertime") val serverTime: String,
    val data: T
)
