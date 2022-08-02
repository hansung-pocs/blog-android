package com.pocs.presentation.model.post.item

import com.pocs.presentation.extension.DatePattern
import com.pocs.presentation.extension.isDateFormat

data class PostItemUiState(
    val id: Int,
    val title: String,
    val writer: String,
    val createdAt: String,
    val canceledAt: String? = null
) {
    val isDeleted: Boolean get() = canceledAt?.isDateFormat(DatePattern.COMPACT) ?: false
}
