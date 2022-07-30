package com.pocs.data.model

data class PostDetailDto(
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val category: String,
    val writer: PostWriterDto
)
