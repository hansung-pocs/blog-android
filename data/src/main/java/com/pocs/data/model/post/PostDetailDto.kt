package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostDetailDto(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("onlyMember") val onlyMember: Boolean,
    @SerializedName("views") val views: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String? = null,
    @SerializedName("category") val category: String,
    @SerializedName("writer") val writer: PostWriterDto
)
