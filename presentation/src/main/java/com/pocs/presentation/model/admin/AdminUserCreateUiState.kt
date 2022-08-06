package com.pocs.presentation.model.admin

import com.pocs.presentation.extension.isValidEmail
import com.pocs.presentation.extension.isValidGithubUserUrl

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
            return createInfo.nickname.isNotEmpty()
                    && createInfo.password.isNotEmpty()
                    && createInfo.name.isNotEmpty()
                    && createInfo.studentId.isNotEmpty()
                    && createInfo.generation.isNotEmpty()
                    && isEmailValid
                    && isGithubUrlValid
        }

    private val isEmailValid = createInfo.email.isValidEmail()
    // 생성 화면이 띄워지고 바로 이메일 입력창만 빨갛게 에러를 보이면 이상하기 때문에 공백일때는 에러를 보이지 않는다.
    val showEmailError = !isEmailValid && createInfo.email.isNotEmpty()

    val isGithubUrlValid = createInfo.github.isEmpty() || createInfo.github.isValidGithubUserUrl()

    fun updateCreateInfo(updater: (UserCreateInfoUiState) -> UserCreateInfoUiState) {
        onUpdateCreateInfo(updater(createInfo))
    }
}
