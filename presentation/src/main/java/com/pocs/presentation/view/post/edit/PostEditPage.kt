package com.pocs.presentation.view.post.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.PostEditUiState
import kotlinx.coroutines.launch

@Composable
fun PostEditPage(title: String, uiState: PostEditUiState) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    PostEditContent(
        title = title,
        onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
        uiState = uiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditContent(
    title: String,
    onBackPressed: () -> Unit,
    uiState: PostEditUiState,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            val coroutineScope = rememberCoroutineScope()

            PostEditAppBar(
                title = title,
                onBackPressed = onBackPressed,
                isInSaving = uiState.isInSaving,
                enableSendIcon = uiState.canSave,
                onClickSend = {
                    if (!uiState.isInSaving) {
                        coroutineScope.launch {
                            val result = uiState.onSave()
                            if (result.isSuccess) {
                                onBackPressed()
                            } else {
                                val exception = result.exceptionOrNull()!!
                                snackBarHostState.showSnackbar(exception.message!!)
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(bottom = 16.dp)
        ) {
            SimpleTextField(
                hint = stringResource(R.string.title),
                value = uiState.title,
                onValueChange = uiState.onChangeTitle,
                modifier = Modifier.fillMaxWidth()
            )
            Divider(startIndent = 16.dp, modifier = Modifier.alpha(0.4f))
            SimpleTextField(
                hint = stringResource(R.string.content),
                value = uiState.content,
                onValueChange = uiState.onChangeContent,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun PostEditAppBar(
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
                SendIconButton(
                    enabled = enableSendIcon,
                    onClick = onClickSend
                )
            }
        }
    )
}

@Composable
fun SimpleTextField(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent
        ),
        placeholder = { Text(hint) },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
    )
}

@Composable
fun SendIconButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Filled.Send,
            contentDescription = stringResource(R.string.save),
            tint = if (enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            }
        )
    }
}

@Preview
@Composable
fun PostEditContentPreview() {
    PostEditContent(
        "게시글 수정",
        {},
        PostEditUiState(
            onChangeTitle = {},
            onChangeContent = {},
            onSave = { Result.success(true) }
        ),
    )
}
