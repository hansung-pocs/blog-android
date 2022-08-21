package com.pocs.presentation.view.component.textfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTextField(
    modifier: Modifier,
    hint: String,
    hintStyle: TextStyle? = null,
    value: String,
    maxLength: Int,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent,
            placeholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        placeholder = { Text(hint, style = hintStyle ?: LocalTextStyle.current) },
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        modifier = modifier,
        trailingIcon = trailingIcon
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTextField(
    modifier: Modifier,
    hint: String,
    value: TextFieldValue,
    maxLength: Int,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (TextFieldValue) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent,
            placeholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        placeholder = { Text(hint) },
        value = value,
        onValueChange = {
            if (it.text.length <= maxLength) {
                onValueChange(it)
            }
        },
        modifier = modifier,
        trailingIcon = trailingIcon
    )
}