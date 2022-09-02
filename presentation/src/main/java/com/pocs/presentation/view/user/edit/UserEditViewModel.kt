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
        requireNotNull(userDetail.defaultInfo) { "유저 편집 화면은 유저 정보가 있는 회원만 접근 할 수 있다." }
        _uiState = mutableStateOf(
            UserEditUiState(
                id = userDetail.id,
                name = userDetail.defaultInfo.name,
                profileImageUrl = userDetail.defaultInfo.profileImageUrl,
                email = userDetail.defaultInfo.email,
                company = userDetail.defaultInfo.company,
                github = userDetail.defaultInfo.github,
                onUpdate = ::update,
                onSave = ::save,
                profileImageUrl = userDetail.defaultInfo.profileImageUrl
            )
        )
    }

    private fun update(uiState: UserEditUiState) {
        _uiState.value = uiState
    }

    private suspend fun save(): Result<Unit> {
        _uiState.value = _uiState.value.copy(isInSaving = true)
        val result = with(uiState.value) {
            updateUserUseCase(
                id = id,
                password = password,
                // TODO: UseCase 가 업데이트되면 newProfileImage 를 전달하도록 수정
                name = name,
                email = email,
                company = company,
                github = github,
                profileImageUrl = profileImageUrl
            )
        }
        _uiState.value = _uiState.value.copy(isInSaving = false)
        return result
    }

    fun messageShown() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}