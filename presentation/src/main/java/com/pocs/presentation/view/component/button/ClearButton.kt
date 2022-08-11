package com.pocs.presentation.view.component.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pocs.presentation.R

@Composable
fun ClearButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = stringResource(R.string.clear_text_field),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
    }
}