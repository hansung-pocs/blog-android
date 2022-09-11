package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostWriterDto(
    @SerializedName("userId") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("type") val type: String?
)
