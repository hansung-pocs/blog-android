package com.pocs.data.model

import com.google.gson.annotations.SerializedName

data class ResponseBody<T>(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int,
    @SerializedName("servertime") val serverTime: String,
    @SerializedName("data") val data: T
)
