package com.pocs.presentation.view.component

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.pocs.presentation.R

/**
 * Shows a AlertDialog when pressing a back button if satisfy a some condition.
 *
 * [navigateUp] is used to navigate up after click the ok button of the dialog.
 */
@Composable
fun RecheckHandler(
    navigateUp: () -> Unit,
    enableRechecking: Boolean = true
) {
    var enabledAlertDialog by remember { mutableStateOf(false) }

    if (enabledAlertDialog) {
        RecheckDialog(
            onDismissRequest = { enabledAlertDialog = false },
            onOkClick = { navigateUp() },
            title = stringResource(R.string.recheck_dialog_title),
            text = stringResource(R.string.recheck_dialog_text)
        )
    }

    BackHandler(enableRechecking) {
        enabledAlertDialog = true
    }
}

@Composable
fun RecheckDialog(
    onOkClick: () -> Unit,
    onDismissRequest: () -> Unit,
    title: String? = null,
    text: String? = null,
    confirmText: String? = null
) {
    AlertDialog(
        title = {
            if (title != null) {
                Text(text = title)
            }
        },
        text = {
            if (text != null) {
                Text(text = text)
            }
        },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text(stringResource(R.string.cancel)) }
        },
        confirmButton = {
            TextButton(onClick = onOkClick) { Text(confirmText ?: stringResource(R.string.ok)) }
        }
    )
}