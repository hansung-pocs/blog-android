package com.pocs.presentation.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.auth.GetCurrentUserStateFlowUseCase
import com.pocs.domain.usecase.auth.IsAuthReadyUseCase
import com.pocs.domain.usecase.auth.LoginUseCase
import com.pocs.presentation.model.auth.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    isAuthReadyUseCase: IsAuthReadyUseCase,
    getCurrentUserStateFlowUseCase: GetCurrentUserStateFlowUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState(onUpdate = ::update))
    val uiState: StateFlow<LoginUiState> get() = _uiState.asStateFlow()

    private var authReadyFetchJob: Job? = null

    init {
        viewModelScope.launch {
            getCurrentUserStateFlowUseCase().collectLatest { currentUser ->
                val isLoggedIn = currentUser != null
                // 이미 로그인 되어있을 때는 `isAuthReady`를 갱신하지 않는다. 그 이유는 앱을 실행하고 곧바로 홈 화면으로
                // 이동시 짧은 시간동안 로그인 화면이 등장하는 버그를 막기 위해서이다.
                // https://github.com/hansung-pocs/blog-android/issues/150
                if (isLoggedIn) {
                    authReadyFetchJob?.cancel()
                }
                _uiState.update { it.copy(isLoggedIn = isLoggedIn) }
            }
        }
        authReadyFetchJob = viewModelScope.launch {
            isAuthReadyUseCase().collectLatest { ready ->
                _uiState.update { it.copy(isAuthReady = ready) }
            }
        }
    }

    private fun update(uiState: LoginUiState) {
        _uiState.value = uiState
    }

    fun login() {
        viewModelScope.launch {
            val result = loginUseCase(
                userName = uiState.value.userName,
                password = uiState.value.password
            )
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(isLoggedIn = true)
                }
            } else {
                val errorMessage = result.exceptionOrNull()!!.message
                _uiState.update {
                    it.copy(errorMessage = errorMessage)
                }
            }
        }
    }

    fun errorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
