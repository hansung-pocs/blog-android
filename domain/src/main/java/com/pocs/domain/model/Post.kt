package com.pocs.domain.model

data class Post(
    val id: Int,
    val title: String,
    val writer: User,
    val content: String?,
    val createdAt: String,
    val updatedAt: String,
    val category: PostCategory
)
