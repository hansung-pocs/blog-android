package com.pocs.data.mapper

import com.pocs.data.model.PostDto
import com.pocs.domain.model.Post
import com.pocs.domain.model.PostCategory
import java.util.*

fun PostDto.toEntity() = Post(
    id = id,
    writer = writerName,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    category = PostCategory.valueOf(category.uppercase(Locale.getDefault()))
)