package com.pocs.presentation.view.user.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.user.GetUserDetailUseCase
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.user.UserDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase
) : ViewModel() {

    var uiState by mutableStateOf<UserDetailUiState>(UserDetailUiState.Loading)
        private set

    private var job: Job? = null

    fun loadUserInfo(id: Int) {
        job?.cancel()
        job = viewModelScope.launch {
            val result = getUserDetailUseCase(id)
            uiState = if (result.isSuccess) {
                val user = result.getOrNull()!!
                UserDetailUiState.Success(user.toUiState())
            } else {
                UserDetailUiState.Failure(
                    e = result.exceptionOrNull()!!,
                    onRetryClick = { loadUserInfo(id) }
                )
            }
        }
    }
}