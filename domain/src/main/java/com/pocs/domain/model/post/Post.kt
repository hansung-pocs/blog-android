package com.pocs.domain.model.post

data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val writer: String? = null,
    val views: Int,
    val onlyMember: Boolean,
    val createdAt: String,
    val updatedAt: String? = null,
    val canceledAt: String? = null,
    val category: PostCategory,
)
