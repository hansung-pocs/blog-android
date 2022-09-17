package com.pocs.presentation.view.component.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.pocs.presentation.R

@Stable
data class DropdownOption(
    val label: String,
    val onClick: () -> Unit,
    val labelColor: Color? = null
)

@Composable
fun DropdownButton(
    options: List<DropdownOption>,
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    IconButton(onClick = { showDropdownMenu = !showDropdownMenu }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.more_info_button)
        )
    }
    DropdownMenu(
        expanded = showDropdownMenu,
        onDismissRequest = { showDropdownMenu = false },
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                modifier = Modifier.testTag(option.label),
                onClick = {
                    option.onClick()
                    showDropdownMenu = false
                },
                text = {
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = option.labelColor ?: MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            )
        }
    }
}
