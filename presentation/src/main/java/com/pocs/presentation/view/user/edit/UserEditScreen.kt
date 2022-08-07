package com.pocs.presentation.view.user.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.constant.MAX_USER_COMPANY_LEN
import com.pocs.presentation.constant.MAX_USER_EMAIL_LEN
import com.pocs.presentation.constant.MAX_USER_GITHUB_LEN
import com.pocs.presentation.constant.MAX_USER_NAME_LEN
import com.pocs.presentation.model.user.UserEditUiState
import com.pocs.presentation.view.component.RecheckHandler
import com.pocs.presentation.view.component.appbar.EditContentAppBar
import com.pocs.presentation.view.component.dialog.PasswordDialog
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
    var showPasswordDialog by remember { mutableStateOf(false) }

    RecheckHandler(navigateUp = navigateUp)

    if (showPasswordDialog) {
        val failedToUpdateString = stringResource(R.string.failed_to_update)

        PasswordDialog(
            onDismissRequest = { showPasswordDialog = false },
            onSaveClick = { password ->
                if (!uiState.isInSaving) {
                    coroutineScope.launch {
                        val result = uiState.onSave(password)
                        if (result.isSuccess) {
                            onSuccessToSave()
                            navigateUp()
                        } else {
                            val exception = result.exceptionOrNull()!!
                            showPasswordDialog = false
                            snackBarHostState.showSnackbar(
                                exception.message ?: failedToUpdateString
                            )
                        }
                    }
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            EditContentAppBar(
                title = stringResource(id = R.string.edit_my_info),
                onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
                isInSaving = uiState.isInSaving,
                enableSendIcon = uiState.canSave,
                onClickSend = { showPasswordDialog = true }
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
            UserEditAvatar {}
            PocsOutlineTextField(
                value = uiState.name,
                label = stringResource(R.string.name),
                maxLength = MAX_USER_NAME_LEN,
                onValueChange = { name ->
                    uiState.update { it.copy(name = name) }
                },
                onClearClick = {
                    uiState.update { it.copy(name = "") }
                }
            )
            PocsOutlineTextField(
                value = uiState.email,
                label = stringResource(id = if (uiState.canSaveEmail) R.string.email else R.string.email_is_not_valid),
                isError = !uiState.canSaveEmail,
                maxLength = MAX_USER_EMAIL_LEN,
                placeholder = stringResource(R.string.email_placeholder),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
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
fun UserEditAvatar(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        // TODO: 회원 사진 정보가 생기면 넣기
        Icon(
            modifier = Modifier
                .size(120.dp)
                .clickable(onClick = onClick),
            imageVector = Icons.Filled.Person,
            contentDescription = stringResource(id = R.string.user_image)
        )
    }
}

@Preview
@Composable
fun UserEditContentPreview() {
    UserEditContent(
        UserEditUiState(
            1,
            "박민석",
            "hello@gmiad.com",
            "google",
            "https://github.com/",
            isInSaving = false,
            {}
        ) { Result.success(Unit) },
        {}
    ) {}
}
