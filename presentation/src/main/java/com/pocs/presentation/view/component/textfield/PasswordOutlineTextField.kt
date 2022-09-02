package com.pocs.presentation.view.component.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.pocs.presentation.R
import com.pocs.presentation.constant.MAX_USER_PASSWORD_LEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordOutlineTextField(
    modifier: Modifier = Modifier,
    password: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Send
    ),
    onPasswordChange: (String) -> Unit,
    onSend: ((String) -> Unit)? = null,
    onClearClick: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    PocsOutlineTextField(
        label = stringResource(id = R.string.password),
        modifier = modifier.fillMaxWidth(),
        value = password,
        onValueChange = {
            onPasswordChange(it)
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onSend = {
                onSend?.invoke(password)
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

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = description,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        },
        onClearClick = onClearClick,
        maxLength = MAX_USER_PASSWORD_LEN
    )
}