package com.pocs.presentation.model.user.item

data class UserItemUiState(
    val id: Int,
    val name: String,
    val studentId: String,
    val generation: Int,
    val canceledAt: String?,
) {
    val isKicked: Boolean get() = canceledAt != null
}
