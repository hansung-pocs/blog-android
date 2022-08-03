package com.pocs.domain.model.post

data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val writer: String,
    val createdAt: String,
    val updatedAt: String,
    val canceledAt: String? = null,
    val category: PostCategory,
)
