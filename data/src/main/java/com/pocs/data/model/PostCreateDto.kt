package com.pocs.data.model

import com.pocs.data.mapper.toDto
import com.pocs.domain.model.PostCategory

data class PostCreateDto(
    val title: String,
    val content: String,
    val userId: Int,
    val category: String
)
