package com.pocs.presentation.view.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.presentation.mock.mockNoticeItemsPagingData
import com.pocs.presentation.model.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy()
            }
        }
    }
}