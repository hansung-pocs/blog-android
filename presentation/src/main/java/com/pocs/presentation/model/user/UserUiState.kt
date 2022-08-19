package com.pocs.presentation.model.user

import androidx.annotation.StringRes
import androidx.paging.PagingData
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.model.user.item.UserItemUiState

data class UserUiState(
    val sortingMethod: UserListSortingMethod = UserListSortingMethod.CREATED_AT,
    val userPagingData: PagingData<UserItemUiState> = PagingData.empty(),
    val enabledSearchMode: Boolean = false,
    val searchPagingData: PagingData<UserItemUiState> = PagingData.empty(),
    val currentUserType: UserType,
    @StringRes val errorMessageRes: Int? = null
)