package com.pocs.data.model.comment

import com.google.gson.annotations.SerializedName

data class CommentUpdateBody(
    @SerializedName("content") val content: String
)
