package com.pocs.presentation.view.home

import androidx.lifecycle.ViewModel
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.presentation.model.post.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    isCurrentUserAdminUseCase: IsCurrentUserAdminUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(isCurrentUserAdmin = isCurrentUserAdminUseCase())
    )
    val uiState: StateFlow<HomeUiState> get() = _uiState.asStateFlow()

    fun showUserMessage(message: String) {
        _uiState.update { it.copy(userMessage = message) }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}