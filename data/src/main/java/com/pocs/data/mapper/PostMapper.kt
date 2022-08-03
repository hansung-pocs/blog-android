package com.pocs.data.mapper

import com.pocs.data.model.post.PostDto
import com.pocs.domain.model.post.Post

fun PostDto.toEntity() = Post(
    id = id,
    writer = writerName,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    canceledAt = canceledAt,
    category = category.toPostCategory()
)