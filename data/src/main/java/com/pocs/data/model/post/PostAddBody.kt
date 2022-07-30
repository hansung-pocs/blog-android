package com.pocs.data.model.post

data class PostAddBody(
    val title: String,
    val content: String,
    val userId: Int,
    val category: String
)
