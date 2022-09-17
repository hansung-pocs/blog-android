package com.pocs.presentation.view.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pocs.presentation.R

@Composable
fun FailureImage(
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier.fillMaxSize(),
        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        imageVector = Icons.Default.Error,
        contentDescription = stringResource(R.string.failed_to_load_image)
    )
}
