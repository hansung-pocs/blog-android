package com.pocs.data.model.post

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("postId") val id: Int,
    val writerName: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val canceledAt: String? = null,
    val category: String
)
