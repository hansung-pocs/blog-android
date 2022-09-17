package com.pocs.presentation.model.user.item

import com.pocs.domain.model.user.UserType
import java.io.Serializable

data class UserDetailItemUiState(
    val id: Int,
    val defaultInfo: UserDefaultInfoUiState?,
    val type: UserType,
    val createdAt: String,
    val canceledAt: String?
) : Serializable {
    val isKicked: Boolean get() = canceledAt != null
}
