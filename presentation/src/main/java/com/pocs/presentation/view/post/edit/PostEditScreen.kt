package com.pocs.presentation.view.post.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foundy.markdown_toolbar.MarkdownToolBar
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.user.UserType
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
import com.pocs.presentation.view.component.checkbox.LabeledCheckBox
import com.pocs.presentation.view.component.textfield.SimpleTextField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PostEditScreen(uiState: PostEditUiState, navigateUp: () -> Unit, onSuccessSave: () -> Unit) {
    val title = if (uiState.category == PostCategory.QNA && uiState.isUserAnonymous) {
        stringResource(R.string.edit_question)
    } else {
        stringResource(R.string.edit_post)
    }
    PostEditContent(
        title = title,
        uiState = uiState,
        navigateUp = navigateUp,
        onSuccessSave = onSuccessSave
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
            Modifier.padding(innerPadding)
        ) {
            val titleContentDescription = stringResource(R.string.title_text_field)
            val contentContentDescription = stringResource(R.string.content_text_field)
            val viewRequester = remember { BringIntoViewRequester() }
            var showToolBar by remember { mutableStateOf(false) }

            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(
                        state = rememberScrollState(),
                        // TODO: https://issuetracker.google.com/issues/192043120 이슈 때문에 엔터키 입력시 자동으로
                        //       커서 위치로 스크롤 되지 않는다. 따라서 일단 아래와 같이 거꾸로 스크롤링 한다. 추후에 위의 이슈가
                        //       해결되면 아래의 코드는 지워도 된다.
                        reverseScrolling = true
                    )
            ) {
                if (uiState.showChips) {
                    PostCategoryChips(
                        isUserAdmin = uiState.isUserAdmin,
                        selectedCategory = uiState.category,
                        onClick = uiState.onCategoryChange
                    )
                }
                if (uiState.showOnlyMemberButton) {
                    LabeledCheckBox(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        checked = uiState.onlyMember,
                        onCheckedChange = uiState.onOnlyMemberChange,
                        label = stringResource(id = R.string.can_see_only_member)
                    )
                }
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
                        // TODO: https://issuetracker.google.com/issues/192043120 에서 문제 해결되면 아래의 두 부분 지우기
                        .bringIntoViewRequester(viewRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    delay(200)
                                    viewRequester.bringIntoView()
                                }
                            }
                        },
                )
                PocsDivider(startIndent = 16.dp)
                SimpleTextField(
                    hint = stringResource(R.string.content),
                    value = uiState.content,
                    maxLength = MAX_POST_CONTENT_LEN,
                    onValueChange = uiState.onContentChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp)
                        .semantics { contentDescription = contentContentDescription }
                        .onFocusChanged { showToolBar = it.hasFocus }
                )
            }
            AnimatedVisibility(visible = showToolBar) {
                MarkdownToolBar(
                    value = uiState.content,
                    onValueChange = uiState.onContentChange
                )
            }
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
        modifier = Modifier.testTag("PostCategoryChips"),
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
            content = TextFieldValue(),
            category = PostCategory.STUDY,
            currentUserType = UserType.ADMIN,
            onTitleChange = {},
            onContentChange = {},
            onCategoryChange = {},
            onSave = { Result.success(Unit) },
            onlyMember = true,
            onOnlyMemberChange = {}
        ),
        navigateUp = {},
        onSuccessSave = {}
    )
}

@Preview
@Composable
fun PostEditContentPreview() {
    PostEditContent(
        "게시글 수정",
        PostEditUiState(
            postId = 1,
            title = "공지입니다.",
            content = TextFieldValue("안녕하세요."),
            category = PostCategory.STUDY,
            currentUserType = UserType.ADMIN,
            onTitleChange = {},
            onContentChange = {},
            onCategoryChange = {},
            onSave = { Result.success(Unit) },
            onlyMember = true,
            onOnlyMemberChange = {}
        ),
        navigateUp = {},
        onSuccessSave = {}
    )
}
