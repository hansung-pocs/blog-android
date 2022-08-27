package com.pocs.domain.model.post

data class PostDetail(
    val id: Int,
    val title: String,
    val writer: PostWriter,
    val views: Int,
    val onlyMember: Boolean,
    val content: String,
    val createdAt: String,
    val updatedAt: String? = null,
    val category: PostCategory
)
