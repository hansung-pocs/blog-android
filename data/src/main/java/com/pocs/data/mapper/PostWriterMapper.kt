package com.pocs.data.mapper

import com.pocs.data.model.post.PostWriterDto
import com.pocs.domain.model.post.PostWriter
import com.pocs.domain.model.user.UserType

fun PostWriterDto.toEntity() = PostWriter(
    id = id,
    name = name,
    email = email,
    // TODO: 임시로 null이면 익명로그인으로 판단하고 있다. 추후에 벡엔드에 유저 타입은 null일 수 없다고 전달해야함
    type = type?.toUserType() ?: UserType.ANONYMOUS,
)