package com.pocs.presentation.view.user.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.presentation.mock.mockUserList
import com.pocs.presentation.model.UserDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    // private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    var uiState by mutableStateOf<UserDetailUiState>(UserDetailUiState.Loading)
        private set

    private var job: Job? = null

    fun loadUserInfo(id: Int) {
        job?.cancel()
        job = viewModelScope.launch {
            val result = Result.success(mockUserList.first { it.id == id }) // getUserUseCase(id)
            uiState = if (result.isSuccess) {
                val user = result.getOrNull()!!
                UserDetailUiState.Success(user)
            } else {
                UserDetailUiState.Failure(e = result.exceptionOrNull()!!, onRetryClick = {
                    loadUserInfo(id)
                })
            }
        }
    }
}