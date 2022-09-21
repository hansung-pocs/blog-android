package com.pocs.presentation.view.home

import androidx.lifecycle.ViewModel
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.presentation.model.post.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getCurrentUserTypeUseCase: GetCurrentUserTypeUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(
        HomeUiState(currentUserType = getCurrentUserTypeUseCase())
    )
    val uiState: StateFlow<HomeUiState> get() = _uiState.asStateFlow()
}
