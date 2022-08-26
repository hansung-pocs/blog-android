package com.pocs.presentation.view.user.anonymous

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.constant.MAX_USER_ID_LEN
import com.pocs.presentation.constant.MAX_USER_PASSWORD_LEN
import com.pocs.presentation.model.user.anonymous.AnonymousCreateInfoUiState
import com.pocs.presentation.model.user.anonymous.AnonymousCreateUiState
import com.pocs.presentation.view.component.RecheckHandler
import com.pocs.presentation.view.component.button.AppBarBackButton
import com.pocs.presentation.view.component.button.PocsButton
import com.pocs.presentation.view.component.textfield.PasswordOutlineTextField
import com.pocs.presentation.view.component.textfield.PocsOutlineTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnonymousCreateScreen(
    uiState: AnonymousCreateUiState,
    navigateUp: () -> Unit,
    onSuccessToCreate: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    var passwordVisible by remember { mutableStateOf(false) }

    if (uiState.isSuccessToCreate) {
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
            SmallTopAppBar(
                navigationIcon = {
                    AppBarBackButton()
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.sign_up_as_anonymous),
                        maxLines = 1
                    )
                }
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
                onPasswordChange = { password ->
                    uiState.updateCreateInfo { it.copy(password = password) }
                },
                onClearClick = {
                    uiState.updateCreateInfo { it.copy(password = "") }
                },
                onSend = { uiState.onCreate() }
            )
            Box(modifier = Modifier.height(16.dp))
            PocsButton(
                label = stringResource(R.string.create_a_temporary_account),
                enabled = uiState.enableCreateButton,
                onClick = { uiState.onCreate() }
            )
            Box(Modifier.height(8.dp))
            // TODO : TEXT 주의사항 추가하기
        }
    }
}

@Composable
@Preview
fun AnonymousCreateContentPreview() {
    AnonymousCreateScreen(
        uiState = AnonymousCreateUiState(
            createInfo = AnonymousCreateInfoUiState(
                userName = "ois0886",
                password = "ㅁㄴㅇㅁㄴㅇ"
            ),
            isInCreating = true,
            onCreate = {},
            isSuccessToCreate = false,
            errorMessage = "asd",
            shownErrorMessage = {},
            onUpdateCreateInfo = {}
        ),
        navigateUp = {},
        onSuccessToCreate = {}
    )
}