package com.pocs.presentation.view.user.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.admin.KickUserUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.user.GetUserDetailUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.user.UserDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val isCurrentUserAdminUseCase: IsCurrentUserAdminUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val kickUserUseCase: KickUserUseCase
) : ViewModel() {

    var uiState by mutableStateOf<UserDetailUiState>(UserDetailUiState.Loading)
        private set

    private var job: Job? = null

    fun fetchUserInfo(id: Int) {
        job?.cancel()
        job = viewModelScope.launch {
            val result = getUserDetailUseCase(id)
            uiState = if (result.isSuccess) {
                val user = result.getOrNull()!!
                UserDetailUiState.Success(
                    userDetail = user.toUiState(),
                    isCurrentUserAdmin = isCurrentUserAdminUseCase(),
                    isMyInfo = getCurrentUserUseCase()?.id == id,
                    shownErrorMessage = ::shownErrorMessage,
                    onKickClick = ::kickUser,
                    successToKick = (uiState as? UserDetailUiState.Success)?.successToKick ?: false
                )
            } else {
                UserDetailUiState.Failure(
                    e = result.exceptionOrNull()!!,
                    onRetryClick = { fetchUserInfo(id) }
                )
            }
        }
    }

    private fun kickUser() {
        val uiState = this.uiState
        if (uiState is UserDetailUiState.Success) {
            assert(uiState.isCurrentUserAdmin)
            viewModelScope.launch {
                val result = kickUserUseCase(uiState.userDetail.id)
                if (result.isSuccess) {
                    this@UserDetailViewModel.uiState = uiState.copy(successToKick = true)
                    fetchUserInfo(uiState.userDetail.id)
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "강퇴에 실패했습니다."
                    this@UserDetailViewModel.uiState = uiState.copy(errorMessage = errorMessage)
                }
            }
        }
    }

    private fun shownErrorMessage() {
        val uiState = uiState
        if (uiState !is UserDetailUiState.Success) return
        this.uiState = uiState.copy(errorMessage = null)
    }
}