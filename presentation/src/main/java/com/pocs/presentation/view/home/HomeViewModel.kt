package com.pocs.presentation.view.home

import androidx.lifecycle.ViewModel
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.post.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getCurrentUserDetailUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(userDetail = getCurrentUserDetailUseCase()?.toUiState())
    )
    val uiState: StateFlow<HomeUiState> get() = _uiState

    val currentUserId: Int?
        get() = uiState.value.userDetail?.id

    val isCurrentUserAdmin: Boolean
        get() = uiState.value.userDetail?.type == UserType.ADMIN
}