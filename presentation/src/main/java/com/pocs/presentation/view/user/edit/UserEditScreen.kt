package com.pocs.presentation.view.user.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.constant.MAX_USER_COMPANY_LEN
import com.pocs.presentation.constant.MAX_USER_EMAIL_LEN
import com.pocs.presentation.constant.MAX_USER_GITHUB_LEN
import com.pocs.presentation.constant.MAX_USER_NAME_LEN
import com.pocs.presentation.model.user.UserEditUiState
import com.pocs.presentation.view.UserAvatar
import com.pocs.presentation.view.component.RecheckHandler
import com.pocs.presentation.view.component.appbar.EditContentAppBar
import com.pocs.presentation.view.component.textfield.PasswordOutlineTextField
import com.pocs.presentation.view.component.textfield.PocsOutlineTextField
import kotlinx.coroutines.launch

@Composable
fun UserEditScreen(
    viewModel: UserEditViewModel,
    navigateUp: () -> Unit,
    onSuccessToSave: () -> Unit
) {
    UserEditContent(
        uiState = viewModel.uiState.value,
        navigateUp = navigateUp,
        onSuccessToSave = onSuccessToSave
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditContent(uiState: UserEditUiState, navigateUp: () -> Unit, onSuccessToSave: () -> Unit) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val failedToUpdateString = stringResource(R.string.failed_to_update)

    RecheckHandler(navigateUp = navigateUp)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            EditContentAppBar(
                title = stringResource(id = R.string.edit_my_info),
                onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
                isInSaving = uiState.isInSaving,
                enableSendIcon = uiState.canSave,
                onClickSend = {
                    if (!uiState.isInSaving) {
                        coroutineScope.launch {
                            val result = uiState.onSave()
                            if (result.isSuccess) {
                                onSuccessToSave()
                                navigateUp()
                            } else {
                                val exception = result.exceptionOrNull()!!
                                snackBarHostState.showSnackbar(
                                    exception.message ?: failedToUpdateString
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            // TODO: onClick 콜백에 이미지 업로드 기능 구현
            UserEditAvatar(profileImageUrl = uiState.profileImageUrl, onClick = {})
            PocsOutlineTextField(
                value = uiState.name,
                label = if (uiState.canSaveName) {
                    stringResource(R.string.name)
                } else {
                    stringResource(R.string.name_must_be_needed)
                },
                modifier = Modifier.semantics { contentDescription = "이름 입력창" },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                isError = !uiState.canSaveName,
                maxLength = MAX_USER_NAME_LEN,
                onValueChange = { name ->
                    uiState.update { it.copy(name = name) }
                },
                onClearClick = {
                    uiState.update { it.copy(name = "") }
                }
            )
            PasswordOutlineTextField(
                password = uiState.password,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                onPasswordChange = { password ->
                    uiState.update { it.copy(password = password) }
                },
                onClearClick = {
                    uiState.update { it.copy(password = "") }
                }
            )
            PocsOutlineTextField(
                value = uiState.email,
                label = if (uiState.canSaveEmail) {
                    stringResource(R.string.email)
                } else if (uiState.email.isEmpty()) {
                    stringResource(R.string.email_must_be_needed)
                } else {
                    stringResource(R.string.email_is_not_valid)
                },
                isError = !uiState.canSaveEmail,
                maxLength = MAX_USER_EMAIL_LEN,
                placeholder = stringResource(R.string.email_placeholder),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { email ->
                    uiState.update { it.copy(email = email) }
                },
                onClearClick = {
                    uiState.update { it.copy(email = "") }
                }
            )
            PocsOutlineTextField(
                value = uiState.company ?: "",
                label = stringResource(R.string.company),
                maxLength = MAX_USER_COMPANY_LEN,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                onValueChange = { company ->
                    uiState.update { it.copy(company = company) }
                },
                onClearClick = {
                    uiState.update { it.copy(company = "") }
                }
            )
            PocsOutlineTextField(
                value = uiState.github ?: "",
                label = stringResource(if (uiState.canSaveGithubUrl) R.string.github else R.string.github_url_is_not_valid),
                isError = !uiState.canSaveGithubUrl,
                maxLength = MAX_USER_GITHUB_LEN,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Next
                ),
                placeholder = stringResource(R.string.github_placeholder),
                onValueChange = { github ->
                    uiState.update { it.copy(github = github) }
                },
                onClearClick = {
                    uiState.update { it.copy(github = "") }
                }
            )
            Box(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun UserEditAvatar(profileImageUrl: String?, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Box {
            UserAvatar(url = profileImageUrl, onClick = onClick)
            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .size(24.dp),
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.user_image)
            )
        }
    }
}

@Preview
@Composable
fun UserEditContentPreview() {
    UserEditContent(
        uiState = UserEditUiState(
            id = 1,
            password = "password",
            name = "박민석",
            email = "hello@gmiad.com",
            profileImageUrl = null,
            company = "google",
            github = "https://github.com/",
            isInSaving = false,
            onUpdate = {},
            onSave = { Result.success(Unit) }
        ),
        navigateUp = {}
    ) {}
}
