package com.pocs.data.model

data class PostAddBody(
    val title: String,
    val content: String,
    val userId: Int,
    val category: String
)
