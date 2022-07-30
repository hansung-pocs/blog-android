package com.pocs.data.mapper

import com.pocs.domain.model.post.PostCategory
import java.util.*

fun String.toPostCategory() = PostCategory.valueOf(this.uppercase(Locale.getDefault()))

fun PostCategory.toDto() = this.toString().lowercase()