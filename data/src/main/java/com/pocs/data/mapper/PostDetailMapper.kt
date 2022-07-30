package com.pocs.data.mapper

import com.pocs.data.model.PostDetailDto
import com.pocs.domain.model.post.PostDetail

fun PostDetailDto.toEntity(id: Int) = PostDetail(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    category = category.toPostCategory(),
    writer = writer.toEntity()
)