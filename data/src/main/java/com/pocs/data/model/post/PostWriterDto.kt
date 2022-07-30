package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostWriterDto(
    @SerializedName("userId") val id: Int,
    @SerializedName("userName") val name: String,
    val email: String,
    val type: String
)
