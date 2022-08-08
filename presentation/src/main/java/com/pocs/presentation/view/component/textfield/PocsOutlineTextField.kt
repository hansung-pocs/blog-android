package com.pocs.presentation.view.component.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PocsOutlineTextField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    placeholder: String? = null,
    maxLength: Int,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    preventToInputEnterKey: Boolean = true
) {
    OutlinedTextField(
        keyboardOptions = keyboardOptions,
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
            if (value.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(R.string.clear_text_field),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
        }
    )
}