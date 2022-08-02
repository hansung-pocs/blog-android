package com.pocs.data.model.post

data class PostUpdateBody(
    val title: String,
    val content: String,
    val userId: Int,
    val category: String
)