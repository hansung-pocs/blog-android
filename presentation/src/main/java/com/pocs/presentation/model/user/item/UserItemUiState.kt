package com.pocs.presentation.model.user.item

data class UserItemUiState(
    val id: Int,
    val defaultInfo: UserDefaultInfoUiState?,
    val canceledAt: String?,
) {
    val isKicked: Boolean get() = canceledAt != null
}
