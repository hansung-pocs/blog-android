package com.pocs.presentation.view.component.button

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pocs.presentation.R

@Composable
fun AppBarBackButton(onBackPressed: (() -> Unit)? = null) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    IconButton(
        onClick = {
            if (onBackPressed == null) {
                onBackPressedDispatcher?.onBackPressed()
            } else {
                onBackPressed.invoke()
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back)
        )
    }
}