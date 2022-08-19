package com.pocs.data.mapper

import com.pocs.domain.model.post.PostFilterType

fun PostFilterType.toDto(): String? {
    // API에서 널인 경우 전체 게시글을 반환하고 있다.
    return if (this == PostFilterType.ALL) {
        null
    } else {
        this.toString().lowercase()
    }
}
