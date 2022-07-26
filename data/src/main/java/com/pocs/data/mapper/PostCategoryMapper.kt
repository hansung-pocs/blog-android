package com.pocs.data.mapper

import com.pocs.domain.model.PostCategory
import java.util.*

fun String.toPostCategory() = PostCategory.valueOf(this.uppercase(Locale.getDefault()))