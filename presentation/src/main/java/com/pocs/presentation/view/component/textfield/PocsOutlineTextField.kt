package com.pocs.presentation.view.component.textfield

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.pocs.presentation.view.component.button.ClearButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PocsOutlineTextField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    placeholder: String? = null,
    maxLength: Int,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    preventToInputEnterKey: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        value = value,
        isError = isError,
        onValueChange = {
            if (it.length <= maxLength) {
                var newValue = it
                if (preventToInputEnterKey) {
                    newValue = newValue.filterNot { char -> char == '\n' }
                }
                onValueChange(newValue)
            }
        },
        label = {
            Text(text = label)
        },
        placeholder = {
            placeholder?.let { Text(text = it) }
        },
        trailingIcon = {
            Row {
                if (value.isNotEmpty()) {
                    ClearButton(onClick = onClearClick)
                }
                trailingIcon?.invoke()
            }
        },
        visualTransformation = visualTransformation
    )
}
