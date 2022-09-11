package com.pocs.data.model.comment

import com.google.gson.annotations.SerializedName

data class CommentsDto(
    @SerializedName("comments") val comments: List<CommentDto>
)