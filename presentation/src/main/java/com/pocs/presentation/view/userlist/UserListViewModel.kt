package com.pocs.presentation.view.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.presentation.mock.mockUsersPagingData
import com.pocs.presentation.model.UserItemUiState
import com.pocs.presentation.model.UserListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UserListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(userPagingData = mockUsersPagingData)
            }
        }
    }
}