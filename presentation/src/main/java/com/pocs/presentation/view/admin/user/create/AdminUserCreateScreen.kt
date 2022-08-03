package com.pocs.presentation.view.admin.user.create

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.R
import com.pocs.presentation.model.admin.AdminUserCreateUiState
import com.pocs.presentation.view.component.RecheckHandler
import com.pocs.presentation.view.component.appbar.EditContentAppBar
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
            PocsOutlineTextField(
                value = createInfo.nickname,
                label = "닉네임",
                onValueChange = { nickname ->
                    uiState.updateCreateInfo { it.copy(nickname = nickname) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(nickname = "") }
                }
            )
            PocsOutlineTextField(
                value = createInfo.password,
                label = "비밀번호",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password ->
                    uiState.updateCreateInfo { it.copy(password = password) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(password = "") }
                }
            )
            PocsOutlineTextField(
                value = createInfo.name,
                label = stringResource(id = R.string.name),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { studentId ->
                    uiState.updateCreateInfo { it.copy(studentId = studentId) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(studentId = "") }
                }
            )
            PocsOutlineTextField(
                value = createInfo.email,
                label = stringResource(id = R.string.email),
                placeholder = stringResource(R.string.email_placeholder),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
            PocsOutlineTextField(
                value = createInfo.company,
                label = stringResource(id = R.string.company),
                onValueChange = { company ->
                    uiState.updateCreateInfo { it.copy(company = company) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(company = "") }
                }
            )
            PocsOutlineTextField(
                value = createInfo.github,
                label = stringResource(id = R.string.github),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                placeholder = stringResource(R.string.github_placeholder),
                onValueChange = { github ->
                    uiState.updateCreateInfo { it.copy(github = github) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(github = "") }
                }
            )
        }
    }
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