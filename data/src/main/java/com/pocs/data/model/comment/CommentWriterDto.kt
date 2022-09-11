package com.pocs.data.model.comment

import com.google.gson.annotations.SerializedName

data class CommentWriterDto(
    @SerializedName("userId") val userId: Int,
    @SerializedName("name") val name: String?
)
