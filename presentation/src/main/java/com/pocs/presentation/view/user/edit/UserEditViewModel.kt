package com.pocs.presentation.view.user.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.user.UserDetail
import com.pocs.presentation.model.UserEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor() : ViewModel() {

    private lateinit var _uiState: MutableState<UserEditUiState>
    val uiState: State<UserEditUiState> get() = _uiState

    fun init(userDetail: UserDetail) {
        _uiState = mutableStateOf(
            UserEditUiState(
                name = userDetail.name,
                email = userDetail.email,
                studentId = userDetail.studentId.toString(),
                company = userDetail.company,
                github = userDetail.github,
                onUpdate = ::update,
                onSave = ::save
            )
        )
    }

    private fun update(uiState: UserEditUiState) {
        _uiState.value = uiState
    }

    private suspend fun save(): Result<Unit> {
        _uiState.value = _uiState.value.copy(isInSaving = true)
        delay(500)
        _uiState.value = _uiState.value.copy(isInSaving = false)
        return Result.success(Unit)
    }
}