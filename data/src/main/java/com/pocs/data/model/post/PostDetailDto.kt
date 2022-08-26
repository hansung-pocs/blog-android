package com.pocs.data.model.post

data class PostDetailDto(
    val title: String,
    val content: String,
    val views: Int,
    val onlyMember: Boolean,
    val createdAt: String,
    val updatedAt: String? = null,
    val category: String,
    val writer: PostWriterDto
)
