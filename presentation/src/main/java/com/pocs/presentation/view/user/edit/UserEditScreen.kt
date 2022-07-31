package com.pocs.presentation.view.user.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.user.UserEditUiState
import com.pocs.presentation.view.component.RecheckHandler
import com.pocs.presentation.view.component.appbar.EditContentAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserEditScreen(viewModel: UserEditViewModel, navigateUp: () -> Unit) {
    UserEditContent(uiState = viewModel.uiState.value, navigateUp = navigateUp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditContent(uiState: UserEditUiState, navigateUp: () -> Unit) {
    val localFocusManager = LocalFocusManager.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var showPasswordDialog by remember { mutableStateOf(false) }

    RecheckHandler(navigateUp = navigateUp)

    if (showPasswordDialog) {
        val failedToUpdateString = stringResource(R.string.failed_to_update)

        UserEditPasswordDialog(
            onDismissRequest = { showPasswordDialog = false },
            onSaveClick = { password ->
                if (!uiState.isInSaving) {
                    coroutineScope.launch {
                        val result = uiState.onSave(password)
                        if (result.isSuccess) {
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
                enableSendIcon = true,
                onClickSend = {
                    localFocusManager.clearFocus()
                    showPasswordDialog = true
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
            UserEditAvatar {}
            UserEditTextField(
                value = uiState.name,
                label = stringResource(R.string.name),
                onValueChange = { name ->
                    uiState.update { it.copy(name = name) }
                },
                onClearClick = {
                    uiState.update { it.copy(name = "") }
                }
            )
            UserEditTextField(
                value = uiState.email,
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.email_placeholder),
                onValueChange = { email ->
                    uiState.update { it.copy(email = email) }
                },
                onClearClick = {
                    uiState.update { it.copy(email = "") }
                }
            )
            UserEditTextField(
                value = uiState.company,
                label = stringResource(R.string.company),
                onValueChange = { company ->
                    uiState.update { it.copy(company = company) }
                },
                onClearClick = {
                    uiState.update { it.copy(company = "") }
                }
            )
            UserEditTextField(
                value = uiState.github,
                label = stringResource(R.string.github),
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

@Composable
fun UserEditTextField(
    value: String,
    label: String,
    placeholder: String? = null,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        placeholder = {
            placeholder?.let { Text(text = it) }
        },
        trailingIcon = {
            IconButton(onClick = onClearClick) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.clear_text_field),
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }
    )
}

@Composable
fun UserEditPasswordDialog(
    onDismissRequest: () -> Unit,
    onSaveClick: (password: String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    UserEditPasswordDialogContent(
        password = password,
        passwordVisible = passwordVisible,
        onPasswordChange = { password = it },
        onClickPasswordVisibleButton = { passwordVisible = !passwordVisible },
        onDismissRequest = onDismissRequest,
        onSaveClick = onSaveClick
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UserEditPasswordDialogContent(
    password: String,
    passwordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onClickPasswordVisibleButton: () -> Unit,
    onDismissRequest: () -> Unit,
    onSaveClick: (password: String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.enter_password)) },
        text = {
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(focusRequester) {
                focusRequester.requestFocus()
                delay(100)
                keyboardController?.show()
            }

            OutlinedTextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = password,
                onValueChange = onPasswordChange,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    val imageVector = if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }

                    val description = if (passwordVisible) {
                        stringResource(R.string.hide_password)
                    } else {
                        stringResource(R.string.show_password)
                    }

                    IconButton(onClick = onClickPasswordVisibleButton) {
                        Icon(imageVector = imageVector, description)
                    }
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = { onSaveClick(password) }, enabled = password.isNotEmpty()) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    )
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
        ) { Result.success(Unit) }
    ) {}
}

@Preview(showBackground = true)
@Composable
fun UserEditPasswordDialogContent() {
    UserEditPasswordDialogContent("1234", false, {}, {}, {}) {}
}