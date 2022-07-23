package com.pocs.domain.model

data class Post(
    val id: Int,
    val title: String,
    val writer: String,
    val createdAt: String,
    val updatedAt: String,
    val category: PostCategory,
)
