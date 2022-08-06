package com.pocs.data.model.post

data class PostDetailDto(
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String? = null,
    val category: String,
    val writer: PostWriterDto
)
