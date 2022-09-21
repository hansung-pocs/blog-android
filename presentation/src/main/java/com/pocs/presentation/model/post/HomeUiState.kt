package com.pocs.presentation.model.post

import com.pocs.domain.model.user.UserType

data class HomeUiState(
    val currentUserType: UserType
) {
    val isCurrentUserAdmin: Boolean get() = currentUserType == UserType.ADMIN
    val isCurrentUserAnonymous: Boolean get() = currentUserType == UserType.ANONYMOUS
}
