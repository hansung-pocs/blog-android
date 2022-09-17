package com.pocs.presentation.view.component.dialog

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pocs.presentation.R
import com.pocs.presentation.view.component.textfield.PasswordOutlineTextField
import kotlinx.coroutines.delay

@Composable
fun PasswordDialog(
    onDismissRequest: () -> Unit,
    onSaveClick: (password: String) -> Unit
) {
    var password by remember { mutableStateOf("") }

    PasswordDialogContent(
        password = password,
        onPasswordChange = { password = it },
        onDismissRequest = onDismissRequest,
        onSaveClick = onSaveClick
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PasswordDialogContent(
    password: String,
    onPasswordChange: (String) -> Unit,
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

            PasswordOutlineTextField(
                modifier = Modifier.focusRequester(focusRequester),
                password = password,
                onPasswordChange = onPasswordChange,
                onSend = onSaveClick,
                onClearClick = { onPasswordChange("") }
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

@Preview(showBackground = true)
@Composable
fun PasswordDialogContent() {
    PasswordDialogContent("1234", {}, {}) {}
}
