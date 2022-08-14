package com.pocs.presentation.view.post.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.R
import com.pocs.presentation.constant.MAX_POST_CONTENT_LEN
import com.pocs.presentation.constant.MAX_POST_TITLE_LEN
import com.pocs.presentation.extension.koreanStringResource
import com.pocs.presentation.model.post.BasePostEditUiState
import com.pocs.presentation.model.post.PostEditUiState
import com.pocs.presentation.view.component.HorizontalChips
import com.pocs.presentation.view.component.PocsDivider
import com.pocs.presentation.view.component.RecheckHandler
import com.pocs.presentation.view.component.appbar.EditContentAppBar
import com.pocs.presentation.view.component.textfield.SimpleTextField
import kotlinx.coroutines.launch

@Composable
fun PostEditScreen(uiState: PostEditUiState, navigateUp: () -> Unit, onSuccessSave: () -> Unit) {
    PostEditContent(
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
            val cannotEditPostString = stringResource(R.string.failed_to_save_post)

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

            PostCategoryChips(
                isUserAdmin = uiState.isUserAdmin,
                selectedCategory = uiState.category,
                onClick = uiState.onCategoryChange
            )
            SimpleTextField(
                hint = stringResource(R.string.title),
                value = uiState.title,
                maxLength = MAX_POST_TITLE_LEN,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                onValueChange = {
                    uiState.onTitleChange(it.filter { char -> char != '\n' })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = titleContentDescription }
            )
            PocsDivider(startIndent = 16.dp)
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
fun PostCategoryChips(
    isUserAdmin: Boolean,
    selectedCategory: PostCategory,
    onClick: (PostCategory) -> Unit
) {
    val categories = remember {
        val posts = PostCategory.values().toMutableList()
        if (!isUserAdmin) {
            posts.remove(PostCategory.NOTICE)
        }
        posts
    }

    HorizontalChips(
        items = categories,
        itemLabelBuilder = { stringResource(id = it.koreanStringResource) },
        selectedItem = selectedCategory,
        onItemClick = onClick
    )
}

@Preview
@Composable
fun PostEditContentEmptyPreview() {
    PostEditContent(
        "게시글 수정",
        PostEditUiState(
            postId = 1,
            title = "",
            content = "",
            category = PostCategory.STUDY,
            isUserAdmin = true,
            onTitleChange = {},
            onContentChange = {},
            onCategoryChange = {},
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
            postId = 1,
            title = "공지입니다.",
            content = "안녕하세요.",
            category = PostCategory.STUDY,
            isUserAdmin = true,
            onTitleChange = {},
            onContentChange = {},
            onCategoryChange = {},
            onSave = { Result.success(Unit) }
        ),
        {}
    ) {}
}
