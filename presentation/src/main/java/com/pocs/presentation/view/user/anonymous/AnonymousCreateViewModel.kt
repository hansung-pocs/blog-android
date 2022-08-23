package com.pocs.presentation.view.user.anonymous

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocs.domain.usecase.user.CreateAnonymousUseCase
import com.pocs.presentation.mapper.toEntity
import com.pocs.presentation.model.user.anonymous.AnonymousCreateInfoUiState
import com.pocs.presentation.model.user.anonymous.AnonymousCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnonymousCreateViewModel @Inject constructor(
    private val createAnonymousUseCase: CreateAnonymousUseCase
) : ViewModel() {

    var uiState by mutableStateOf(
        AnonymousCreateUiState(
            onUpdateCreateInfo = ::updateInfo,
            onCreate = ::createUser,
            shownErrorMessage = ::shownErrorMessage
        )
    )
        private set

    private fun updateInfo(info: AnonymousCreateInfoUiState) {
        this.uiState = this.uiState.copy(createInfo = info)
    }

    private fun createUser() {
        viewModelScope.launch {
            uiState = uiState.copy(isInCreating = true)
            val result = createAnonymousUseCase(uiState.createInfo.toEntity())
            uiState = uiState.copy(isInCreating = false)

            uiState = if (result.isSuccess) {
                uiState.copy(isSuccessToCreate = true)
            } else {
                val message = result.exceptionOrNull()!!.message ?: "생성에 실패했습니다."
                uiState.copy(errorMessage = message)
            }
        }
    }

    private fun shownErrorMessage() {
        uiState = uiState.copy(errorMessage = null)
    }
}