package com.pocs.data.mapper

import com.pocs.data.model.PostWriterDto
import com.pocs.domain.model.post.PostWriter

fun PostWriterDto.toEntity() = PostWriter(
    id = id,
    name = name,
    email = email,
    type = type.toUserType(),
)