package com.pocs.presentation.view.user.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.UserEditUiState
import com.pocs.presentation.view.common.RecheckHandler
import com.pocs.presentation.view.common.appbar.EditContentAppBar
import kotlinx.coroutines.launch

@Composable
fun UserEditScreen(viewModel: UserEditViewModel, navigateUp: () -> Unit) {
    UserEditContent(uiState = viewModel.uiState.value, navigateUp = navigateUp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditContent(uiState: UserEditUiState, navigateUp: () -> Unit) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    RecheckHandler(navigateUp = navigateUp)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            EditContentAppBar(
                title = stringResource(id = R.string.edit_my_info),
                onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
                isInSaving = uiState.isInSaving,
                enableSendIcon = true,
                onClickSend = {
                    if (!uiState.isInSaving) {
                        coroutineScope.launch {
                            val result = uiState.onSave()
                            if (result.isSuccess) {
                                navigateUp()
                            } else {
                                val exception = result.exceptionOrNull()!!
                                snackBarHostState.showSnackbar(exception.message!!)
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
                value = uiState.studentId,
                label = stringResource(R.string.student_id),
                onValueChange = { studentId ->
                    uiState.update { it.copy(studentId = studentId) }
                },
                onClearClick = {
                    uiState.update { it.copy(studentId = "") }
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

@Preview
@Composable
fun UserEditContentPreview() {
    UserEditContent(
        UserEditUiState(
            "박민석",
            "hello@gmiad.com",
            "18294012",
            "google",
            "https://github.com/",
            isInSaving = false,
            {}
        ) { Result.success(Unit) }
    ) {}
}