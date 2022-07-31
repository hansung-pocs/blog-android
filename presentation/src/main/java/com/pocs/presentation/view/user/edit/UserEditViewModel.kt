package com.pocs.presentation.view.user.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pocs.domain.usecase.user.UpdateUserUseCase
import com.pocs.presentation.model.user.item.UserDetailItemUiState
import com.pocs.presentation.model.user.UserEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    private lateinit var _uiState: MutableState<UserEditUiState>
    val uiState: State<UserEditUiState> get() = _uiState

    fun init(userDetail: UserDetailItemUiState) {
        _uiState = mutableStateOf(
            UserEditUiState(
                id = userDetail.id,
                name = userDetail.name,
                email = userDetail.email,
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

    private suspend fun save(password: String): Result<Unit> {
        _uiState.value = _uiState.value.copy(isInSaving = true)
        val result = with(uiState.value) {
            updateUserUseCase(
                id = id,
                password = password,
                name = name,
                email = email,
                company = company,
                github = github
            )
        }
        _uiState.value = _uiState.value.copy(isInSaving = false)
        return result
    }
}