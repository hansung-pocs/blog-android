package com.pocs.presentation.view.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.GetMyUserInfoUseCase
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.model.user.item.UserDetailItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getMyUserInfoUseCase: GetMyUserInfoUseCase,
) : ViewModel() {

    private lateinit var _uiState: MutableState<UserDetailItemUiState>
    val uiState: State<UserDetailItemUiState> get() = _uiState

    val userDetail: UserDetail = getMyUserInfoUseCase()

    fun init() {
        _uiState = mutableStateOf(
            UserDetailItemUiState(
                id = userDetail.id,
                name = userDetail.name,
                email = userDetail.email,
                studentId = userDetail.studentId,
                type = userDetail.type,
                company = userDetail.company,
                generation = userDetail.generation,
                github = userDetail.github,
                createdAt = userDetail.createdAt,
                canceledAt = userDetail.canceledAt,
            )
        )
    }
}