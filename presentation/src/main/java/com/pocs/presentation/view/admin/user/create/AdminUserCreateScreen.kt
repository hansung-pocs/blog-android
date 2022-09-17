package com.pocs.presentation.view.admin.user.create

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.R
import com.pocs.presentation.constant.*
import com.pocs.presentation.model.admin.AdminUserCreateUiState
import com.pocs.presentation.view.component.RecheckHandler
import com.pocs.presentation.view.component.appbar.EditContentAppBar
import com.pocs.presentation.view.component.textfield.PasswordOutlineTextField
import com.pocs.presentation.view.component.textfield.PocsOutlineTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserCreateScreen(
    uiState: AdminUserCreateUiState,
    navigateUp: () -> Unit,
    onSuccessToCreate: () -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val snackBarHostState = remember { SnackbarHostState() }

    if (uiState.isSuccessToSave) {
        onSuccessToCreate()
        navigateUp()
    }
    if (uiState.errorMessage != null) {
        LaunchedEffect(uiState.errorMessage) {
            snackBarHostState.showSnackbar(uiState.errorMessage)
            uiState.shownErrorMessage()
        }
    }

    RecheckHandler(navigateUp = navigateUp)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            EditContentAppBar(
                title = stringResource(id = R.string.create_user),
                onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
                isInSaving = uiState.isInSaving,
                enableSendIcon = uiState.canSave,
                onClickSend = { uiState.onSave() }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        val createInfo = uiState.createInfo

        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            EditGroupLabel("필수")
            PocsOutlineTextField(
                value = createInfo.userName,
                label = stringResource(R.string.id),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                maxLength = MAX_USER_ID_LEN,
                onValueChange = { userName ->
                    uiState.updateCreateInfo { it.copy(userName = userName) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(userName = "") }
                }
            )
            PasswordOutlineTextField(
                password = createInfo.password,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                onPasswordChange = { password ->
                    uiState.updateCreateInfo { it.copy(password = password) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(password = "") }
                }
            )
            PocsOutlineTextField(
                value = createInfo.name,
                label = stringResource(id = R.string.name),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                maxLength = MAX_USER_NAME_LEN,
                onValueChange = { name ->
                    uiState.updateCreateInfo { it.copy(name = name) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(name = "") }
                }
            )
            PocsOutlineTextField(
                value = createInfo.studentId,
                label = stringResource(id = R.string.student_id),
                maxLength = MAX_USER_STUDENT_ID_LEN,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { studentId ->
                    uiState.updateCreateInfo { it.copy(studentId = studentId) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(studentId = "") }
                }
            )
            PocsOutlineTextField(
                value = createInfo.email,
                label = stringResource(
                    if (uiState.showEmailError) {
                        R.string.email_is_not_valid
                    } else {
                        R.string.email
                    }
                ),
                isError = uiState.showEmailError,
                maxLength = MAX_USER_EMAIL_LEN,
                placeholder = stringResource(R.string.email_placeholder),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { email ->
                    uiState.updateCreateInfo { it.copy(email = email) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(email = "") }
                }
            )
            PocsOutlineTextField(
                value = createInfo.generation,
                label = stringResource(id = R.string.generation),
                maxLength = MAX_USER_GENERATION_LEN,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { generation ->
                    uiState.updateCreateInfo { it.copy(generation = generation) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(generation = "") }
                }
            )
            UserTypeDropdownMenu(
                selectedType = createInfo.type,
                onItemClick = { userType ->
                    uiState.updateCreateInfo { it.copy(type = userType) }
                }
            )
            EditGroupLabel("선택")
            PocsOutlineTextField(
                value = createInfo.company,
                label = stringResource(id = R.string.company),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                maxLength = MAX_USER_COMPANY_LEN,
                onValueChange = { company ->
                    uiState.updateCreateInfo { it.copy(company = company) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(company = "") }
                }
            )
            PocsOutlineTextField(
                value = createInfo.github,
                label = stringResource(
                    if (uiState.canSaveGithubUrl) {
                        R.string.github
                    } else {
                        R.string.github_url_is_not_valid
                    }
                ),
                isError = !uiState.canSaveGithubUrl,
                maxLength = MAX_USER_GITHUB_LEN,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Next
                ),
                placeholder = stringResource(R.string.github_placeholder),
                onValueChange = { github ->
                    uiState.updateCreateInfo { it.copy(github = github) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(github = "") }
                }
            )
            Box(Modifier.height(8.dp))
        }
    }
}

@Composable
fun EditGroupLabel(text: String) {
    Text(
        text,
        modifier = Modifier.padding(top = 16.dp),
        style = MaterialTheme.typography.labelMedium.copy(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTypeDropdownMenu(selectedType: UserType, onItemClick: (UserType) -> Unit) {
    val options = remember { listOf(UserType.ADMIN, UserType.MEMBER) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            readOnly = true,
            value = selectedType.koreanString,
            onValueChange = { },
            label = { Text(stringResource(R.string.type)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { userType ->
                DropdownMenuItem(
                    onClick = {
                        onItemClick(userType)
                        expanded = false
                    },
                    text = { Text(text = userType.koreanString) }
                )
            }
        }
    }
}
