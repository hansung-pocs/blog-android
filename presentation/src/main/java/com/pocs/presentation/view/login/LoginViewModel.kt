package com.pocs.presentation.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.auth.LoginUseCase
import com.pocs.presentation.model.auth.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState(onUpdate = ::update))
    val uiState: StateFlow<LoginUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
            _uiState.update {
                it.copy(isLoggedIn = currentUser != null)
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
