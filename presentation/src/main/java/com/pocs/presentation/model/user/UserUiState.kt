package com.pocs.presentation.model.user

import androidx.paging.PagingData
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.model.user.item.UserItemUiState

data class UserUiState(
    val sortingMethod: UserListSortingMethod = UserListSortingMethod.GENERATION,
    val userPagingData: PagingData<UserItemUiState> = PagingData.empty(),
    val currentUserType: UserType
)