package com.pocs.presentation.model.admin

import com.pocs.presentation.constant.MIN_USER_PASSWORD_LEN
import com.pocs.presentation.extension.canSaveAsGithubUrl
import com.pocs.presentation.extension.isValidEmail

data class AdminUserCreateUiState(
    val createInfo: UserCreateInfoUiState = UserCreateInfoUiState(),
    val isInSaving: Boolean = false,
    val onSave: () -> Unit,
    val isSuccessToSave: Boolean = false,
    val errorMessage: String? = null,
    val shownErrorMessage: () -> Unit,
    private val onUpdateCreateInfo: (UserCreateInfoUiState) -> Unit,
) {
    val canSave
        get() : Boolean {
            return createInfo.userName.isNotEmpty()
                    && createInfo.password.length >= MIN_USER_PASSWORD_LEN
                    && createInfo.name.isNotEmpty()
                    && createInfo.studentId.isNotEmpty()
                    && createInfo.generation.isNotEmpty()
                    && canSaveEmail
                    && canSaveGithubUrl
        }

    private val canSaveEmail = createInfo.email.isValidEmail()

    // 생성 화면이 띄워지고 바로 이메일 입력창만 빨갛게 에러를 보이면 이상하기 때문에 공백일때는 에러를 보이지 않는다.
    val showEmailError = !canSaveEmail && createInfo.email.isNotEmpty()

    val canSaveGithubUrl = createInfo.github.canSaveAsGithubUrl()

    fun updateCreateInfo(updater: (UserCreateInfoUiState) -> UserCreateInfoUiState) {
        onUpdateCreateInfo(updater(createInfo))
    }
}
