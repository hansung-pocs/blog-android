package com.pocs.presentation.view.post.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.R
import com.pocs.presentation.constant.MAX_POST_CONTENT_LEN
import com.pocs.presentation.constant.MAX_POST_TITLE_LEN
import com.pocs.presentation.model.BasePostEditUiState
import com.pocs.presentation.model.post.PostEditUiState
import com.pocs.presentation.view.component.RecheckHandler
import com.pocs.presentation.view.component.appbar.EditContentAppBar
import kotlinx.coroutines.launch

@Composable
fun PostEditScreen(uiState: PostEditUiState, navigateUp: () -> Unit, onSuccessSave: () -> Unit) {
    PostEditContent(
        // TODO: 게시글 속성에 따라 "OOO 편집"과 같이 다르게 보이기
        title = stringResource(id = R.string.edit_post),
        uiState = uiState,
        navigateUp = navigateUp,
        onSuccessSave = onSuccessSave
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditContent(
    title: String,
    uiState: BasePostEditUiState,
    navigateUp: () -> Unit,
    onSuccessSave: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    RecheckHandler(
        navigateUp = navigateUp,
        enableRechecking = !uiState.isEmpty
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            val cannotEditPostString = stringResource(R.string.cannot_edit_post)

            EditContentAppBar(
                title = title,
                onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
                isInSaving = uiState.isInSaving,
                enableSendIcon = uiState.canSave,
                onClickSend = {
                    if (!uiState.isInSaving) {
                        coroutineScope.launch {
                            val result = uiState.onSave()
                            if (result.isSuccess) {
                                onSuccessSave()
                                navigateUp()
                            } else {
                                val exception = result.exceptionOrNull()!!
                                snackBarHostState.showSnackbar(
                                    exception.message ?: cannotEditPostString
                                )
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
            val titleContentDescription = stringResource(R.string.title_text_field)
            val contentContentDescription = stringResource(R.string.content_text_field)

            SimpleTextField(
                hint = stringResource(R.string.title),
                value = uiState.title,
                maxLength = MAX_POST_TITLE_LEN,
                onValueChange = {
                    uiState.onTitleChange(it.filter { char -> char != '\n' })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = titleContentDescription }
            )
            Divider(startIndent = 16.dp, modifier = Modifier.alpha(0.4f))
            SimpleTextField(
                hint = stringResource(R.string.content),
                value = uiState.content,
                maxLength = MAX_POST_CONTENT_LEN,
                onValueChange = uiState.onContentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .semantics { contentDescription = contentContentDescription }
            )
        }
    }
}

@Composable
fun SimpleTextField(
    hint: String,
    value: String,
    maxLength: Int,
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
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun PostEditContentEmptyPreview() {
    PostEditContent(
        "게시글 수정",
        PostEditUiState(
            id = 1,
            title = "",
            content = "",
            category = PostCategory.STUDY,
            onTitleChange = {},
            onContentChange = {},
            onSave = { Result.success(Unit) }
        ),
        {}
    ) {}
}

@Preview
@Composable
fun PostEditContentPreview() {
    PostEditContent(
        "게시글 수정",
        PostEditUiState(
            id = 1,
            title = "공지입니다.",
            content = "안녕하세요.",
            category = PostCategory.STUDY,
            onTitleChange = {},
            onContentChange = {},
            onSave = { Result.success(Unit) }
        ),
        {}
    ) {}
}
