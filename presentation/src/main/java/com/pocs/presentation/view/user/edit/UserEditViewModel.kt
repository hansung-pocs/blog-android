package com.pocs.presentation.view.user.edit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pocs.domain.usecase.user.UpdateUserUseCase
import com.pocs.domain.usecase.util.ConvertBitmapToFileUseCase
import com.pocs.presentation.model.user.UserEditUiState
import com.pocs.presentation.model.user.item.UserDetailItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val convertBitmapToFileUseCase: ConvertBitmapToFileUseCase
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
                onSave = ::save
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
                name = name,
                email = email,
                company = company,
                github = github,
                useDefaultProfileImage = useDefaultProfileImage,
                newProfileImage = newProfileImage?.let {
                    convertBitmapToFileUseCase(it, "userProfileImage.png")
                }
            )
        }
        _uiState.value = _uiState.value.copy(isInSaving = false)
        return result
    }

    fun messageShown() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}
