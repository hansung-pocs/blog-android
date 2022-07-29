package com.pocs.presentation.view.user.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pocs.domain.model.User
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
                onSave = ::save
            )
        )
    }

    private suspend fun save(): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }
}