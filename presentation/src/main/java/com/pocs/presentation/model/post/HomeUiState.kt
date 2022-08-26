package com.pocs.presentation.model.post

import com.pocs.domain.model.user.UserType

data class HomeUiState(
    val currentUserType: UserType,
    val userMessage: String? = null
) {
    val isCurrentUserAdmin: Boolean get() = currentUserType == UserType.ADMIN
    val isCurrentUserAnonymous: Boolean get() = currentUserType == UserType.ANONYMOUS
}