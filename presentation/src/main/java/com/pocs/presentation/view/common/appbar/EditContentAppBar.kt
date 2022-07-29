package com.pocs.presentation.view.common.appbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.view.common.button.SendButton

@Composable
fun EditContentAppBar(
    title: String,
    onBackPressed: () -> Unit,
    isInSaving: Boolean,
    enableSendIcon: Boolean,
    onClickSend: () -> Unit
) {
    SmallTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            if (isInSaving) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .testTag("CircularProgressIndicator")
                        .padding(8.dp)
                )
            } else {
                SendButton(
                    enabled = enableSendIcon,
                    onClick = onClickSend
                )
            }
        }
    )
}