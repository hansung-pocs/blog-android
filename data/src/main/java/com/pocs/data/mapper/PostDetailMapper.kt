package com.pocs.data.mapper

import com.pocs.data.model.PostDetailDto
import com.pocs.data.model.PostDto
import com.pocs.data.model.UserDto
import com.pocs.domain.model.Post
import com.pocs.domain.model.PostCategory
import com.pocs.domain.model.PostDetail
import java.util.*

fun PostDetailDto.toEntity(id: Int) = PostDetail(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    category = category.toPostCategory(),
    writer = writer.toEntity()
)