package com.pocs.presentation.view.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.constant.MAX_USER_NICKNAME_LEN
import com.pocs.presentation.model.auth.LoginUiState
import com.pocs.presentation.view.component.button.PocsButton
import com.pocs.presentation.view.component.textfield.PasswordOutlineTextField
import com.pocs.presentation.view.component.textfield.PocsOutlineTextField

@Composable
fun LoginScreen(viewModel: LoginViewModel, onBrowseAsNonMemberClick: () -> Unit) {
    val uiState = viewModel.uiState.collectAsState()

    LoginContent(
        uiState = uiState.value,
        onLoginClick = viewModel::login,
        onBrowseAsNonMemberClick = onBrowseAsNonMemberClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    uiState: LoginUiState,
    onLoginClick: () -> Unit,
    onBrowseAsNonMemberClick: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val errorMessage = uiState.errorMessage

    if (errorMessage != null) {
        LaunchedEffect(errorMessage) {
            snackBarHostState.showSnackbar(message = errorMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            LoginTitle()
            PocsOutlineTextField(
                value = uiState.userName,
                label = stringResource(R.string.id),
                onValueChange = { userName ->
                    uiState.update { it.copy(userName = userName) }
                },
                onClearClick = {
                    uiState.update { it.copy(userName = "") }
                },
                maxLength = MAX_USER_NICKNAME_LEN,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            PasswordOutlineTextField(
                password = uiState.password,
                onPasswordChange = { password ->
                    uiState.update { it.copy(password = password) }
                },
                onClearClick = {
                    uiState.update { it.copy(password = "") }
                },
                onSend = { onLoginClick() }
            )
            Box(modifier = Modifier.height(16.dp))
            PocsButton(label = stringResource(id = R.string.login), onClick = onLoginClick)
            TextButton(onClick = onBrowseAsNonMemberClick) {
                Text(
                    text = stringResource(R.string.brows_as_non_member),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4F)
                )
            }
        }
    }
}

@Composable
fun LoginTitle() {
    Text(
        modifier = Modifier.padding(vertical = 64.dp),
        text = stringResource(R.string.login_title),
        style = MaterialTheme.typography.displayLarge.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Black
        )
    )
}

@Preview
@Composable
fun LoginContentPreview() {
    LoginContent(
        uiState = LoginUiState(
            isLoggedIn = false,
            errorMessage = null,
            userName = "",
            password = "",
            onUpdate = {}
        ),
        onLoginClick = {},
        onBrowseAsNonMemberClick = {}
    )
}