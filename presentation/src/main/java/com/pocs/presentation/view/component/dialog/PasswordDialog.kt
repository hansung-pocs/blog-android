package com.pocs.presentation.view.component.dialog

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.pocs.presentation.R
import kotlinx.coroutines.delay

@Composable
fun PasswordDialog(
    onDismissRequest: () -> Unit,
    onSaveClick: (password: String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    PasswordDialogContent(
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
private fun PasswordDialogContent(
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        onSaveClick(password)
                    }
                ),
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


@Preview(showBackground = true)
@Composable
fun PasswordDialogContent() {
    PasswordDialogContent("1234", false, {}, {}, {}) {}
}