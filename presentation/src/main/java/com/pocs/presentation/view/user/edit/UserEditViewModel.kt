package com.pocs.presentation.view.user.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.user.User
import com.pocs.presentation.model.UserEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor() : ViewModel() {

    private lateinit var _uiState: MutableState<UserEditUiState>
    val uiState: State<UserEditUiState> get() = _uiState

    fun init(user: User) {
        _uiState = mutableStateOf(
            UserEditUiState(
                name = user.name,
                email = user.email,
                studentId = user.studentId.toString(),
                company = user.company,
                github = user.github,
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