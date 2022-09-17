package com.pocs.presentation.view.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.usecase.auth.GetCurrentUserStateFlowUseCase
import com.pocs.domain.usecase.auth.LogoutUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.setting.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getCurrentUserStateFlowUseCase: GetCurrentUserStateFlowUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    var uiState by mutableStateOf(
        SettingUiState(
            currentUser = requireNotNull(getCurrentUserStateFlowUseCase().value).toUiState()
        )
    )
        private set

    init {
        viewModelScope.launch {
            getCurrentUserStateFlowUseCase().collectLatest {
                // 로그아웃 요청 후 성공 응답을 함수에서 반환하기 전에 currentUserStateFlow가 먼저 null로 바꿘다.
                // 이 때문에 null값이 Flow를 통해 전달되기 때문에, null일때를 예외한다.
                if (it != null) {
                    uiState = uiState.copy(currentUser = it.toUiState())
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = logoutUseCase()

            uiState = if (result.isSuccess) {
                uiState.copy(onSuccessToLogout = true)
            } else {
                uiState.copy(errorMessage = result.exceptionOrNull()!!.message)
            }
        }
    }

    fun errorMessageShown() {
        uiState = uiState.copy(errorMessage = null)
    }

    private fun requireNotNull(userDetail: UserDetail?): UserDetail {
        return requireNotNull(
            userDetail,
            lazyMessage = { "설정화면은 로그인할때만 접근 가능하기 때문에 유저가 null 일 수 없다." }
        )
    }
}
