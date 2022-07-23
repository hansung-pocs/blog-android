package com.pocs.presentation.view.post.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.PostEditUiState
import kotlinx.coroutines.launch

@Composable
fun PostEditPage(uiState: PostEditUiState) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    PostEditContent(
        popBack = { onBackPressedDispatcher?.onBackPressed() },
        uiState = uiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditContent(
    popBack: () -> Unit,
    uiState: PostEditUiState,
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = stringResource(R.string.edit_post))
                },
                navigationIcon = {
                    IconButton(
                        onClick = popBack
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            val scope = rememberCoroutineScope()

            FloatingActionButton(
                onClick = {
                    if (!uiState.isInSaving) {
                        scope.launch {
                            uiState.onSave()
                            popBack()
                        }
                    }
                }
            ) {
                if (uiState.isInSaving) {
                    CircularProgressIndicator()
                } else {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(R.string.save)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 0.dp)
        ) {
            PocsTextField(
                hint = stringResource(R.string.title),
                value = uiState.title,
                onValueChange = uiState.onChangeTitle,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            PocsTextField(
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
fun PocsTextField(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        shape = RoundedCornerShape(8.dp),
        placeholder = { Text(hint) },
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
    )
}

@Preview
@Composable
fun PostEditContentPreview() {
    PostEditContent(
        {},
        PostEditUiState(
            onChangeTitle = {},
            onChangeContent = {},
            onSave = { Result.success(true) }
        ),
    )
}
