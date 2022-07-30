package com.pocs.domain.model.post

import com.pocs.domain.model.user.User

data class PostDetail(
    val id: Int,
    val title: String,
    val writer: User,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val category: PostCategory
)
