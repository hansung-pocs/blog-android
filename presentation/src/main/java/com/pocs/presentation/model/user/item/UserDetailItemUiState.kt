package com.pocs.presentation.model.user.item

import com.pocs.domain.model.user.UserType
import com.pocs.presentation.extension.DatePattern
import com.pocs.presentation.extension.isDateFormat
import java.io.Serializable

data class UserDetailItemUiState(
    val id: Int,
    val name: String,
    val email: String,
    val studentId: Int,
    val type: UserType,
    val company: String,
    val generation: Int,
    val github: String,
    val createdAt: String,
    val canceledAt: String,
) : Serializable {
    val isKicked: Boolean get() = canceledAt.isDateFormat(DatePattern.COMPACT)
}