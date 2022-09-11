package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("postId") val id: Int,
    @SerializedName("writerName") val writerName: String? = null,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("views") val views: Int,
    @SerializedName("onlyMember") val onlyMember: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String? = null,
    @SerializedName("canceledAt") val canceledAt: String? = null,
    @SerializedName("category") val category: String
)
