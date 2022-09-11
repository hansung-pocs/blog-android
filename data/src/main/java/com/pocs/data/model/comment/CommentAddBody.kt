package com.pocs.data.model.comment

import com.google.gson.annotations.SerializedName

data class CommentAddBody(
    @SerializedName("postId") val postId: Int,
    @SerializedName("parentId") val parentId: Int?,
    @SerializedName("content") val content: String
)
