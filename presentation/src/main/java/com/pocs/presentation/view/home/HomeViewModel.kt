package com.pocs.presentation.view.home

import androidx.lifecycle.ViewModel
import com.pocs.domain.usecase.user.GetCurrentUserDetailUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.post.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getCurrentUserDetailUseCase: GetCurrentUserDetailUseCase,
) : ViewModel() {

    private var _uiState: MutableStateFlow<HomeUiState>
    val uiState: StateFlow<HomeUiState> get() = _uiState

    init {
        val userDetail = getCurrentUserDetailUseCase()
        _uiState = MutableStateFlow(
            HomeUiState(
                userDetail = userDetail.toUiState()
            )
        )
    }
}