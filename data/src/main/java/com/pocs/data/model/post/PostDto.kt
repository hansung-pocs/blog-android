package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("postId") val id: Int,
    val writerName: String? = null,
    val title: String,
    val content: String,
    val views: Int,
    val onlyMember: Boolean,
    val createdAt: String,
    val updatedAt: String? = null,
    val canceledAt: String? = null,
    val category: String
)
