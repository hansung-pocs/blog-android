package com.pocs.data.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("postId") val id: Int,
    val writerName: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val category: String
)
