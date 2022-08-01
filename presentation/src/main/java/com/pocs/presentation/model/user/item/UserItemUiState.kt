package com.pocs.presentation.model.user.item

import com.pocs.presentation.extension.DatePattern
import com.pocs.presentation.extension.isDateFormat

data class UserItemUiState(
    val id: Int,
    val name: String,
    val studentId: String,
    val generation: Int,
    val canceledAt: String
) {
    val isKicked: Boolean get() = canceledAt.isDateFormat(DatePattern.COMPACT)
}
