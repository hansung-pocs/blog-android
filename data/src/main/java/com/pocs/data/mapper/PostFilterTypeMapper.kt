package com.pocs.data.mapper

import com.pocs.domain.model.post.PostFilterType

fun PostFilterType.toDto() = this.toString().lowercase()
