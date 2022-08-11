package com.pocs.presentation.view.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.auth.GetCurrentUserStateFlowUseCase
import com.pocs.domain.usecase.auth.LogoutUseCase
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

    var uiState by mutableStateOf(SettingUiState())
        private set

    init {
        viewModelScope.launch {
            getCurrentUserStateFlowUseCase().collectLatest {
                uiState = uiState.copy(currentUser = it)
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
}