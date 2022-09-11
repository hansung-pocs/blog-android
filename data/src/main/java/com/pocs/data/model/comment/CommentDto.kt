package com.pocs.data.model.comment

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("commentId") val commentId: Int,
    @SerializedName("parentId") val parentId: Int,
    @SerializedName("childrenCount") val childrenCount: Int,
    @SerializedName("postId") val postId: Int,
    @SerializedName("writer") val writer: CommentWriterDto,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String? = null,
    @SerializedName("canceledAt") val canceledAt: String? = null
)
