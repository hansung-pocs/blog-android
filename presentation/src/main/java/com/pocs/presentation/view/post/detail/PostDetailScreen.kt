package com.pocs.presentation.view.post.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.R
import com.pocs.presentation.model.comment.CommentsUiState
import com.pocs.presentation.model.comment.item.CommentItemUiState
import com.pocs.presentation.model.comment.item.CommentWriterUiState
import com.pocs.presentation.model.post.PostDetailUiState
import com.pocs.presentation.model.post.item.PostDetailItemUiState
import com.pocs.presentation.model.post.item.PostWriterUiState
import com.pocs.presentation.view.component.*
import com.pocs.presentation.view.component.bottomsheet.CommentModalBottomSheet
import com.pocs.presentation.view.component.bottomsheet.CommentSendCallback
import com.pocs.presentation.view.component.button.AppBarBackButton
import com.pocs.presentation.view.component.button.DropdownButton
import com.pocs.presentation.view.component.button.DropdownOption
import kotlinx.coroutines.launch

@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel,
    onEditClick: () -> Unit,
    onDeleteSuccess: () -> Unit,
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is PostDetailUiState.Failure -> {
            PostDetailFailureContent(uiState = uiState)
        }
        PostDetailUiState.Loading -> {
            LoadingContent()
        }
        is PostDetailUiState.Success -> {
            val snackbarHostState = remember { SnackbarHostState() }

            if (uiState.isDeleteSuccess) {
                onDeleteSuccess()
            }
            if (uiState.userMessage != null) {
                LaunchedEffect(uiState.userMessage) {
                    snackbarHostState.showSnackbar(uiState.userMessage)
                    viewModel.userMessageShown()
                }
            }

            PostDetailContent(
                uiState = uiState,
                snackbarHostState = snackbarHostState,
                onEditClick = onEditClick,
                onDeleteClick = { viewModel.requestPostDeleting(uiState.postDetail.id) },
                onSend = viewModel::addComment
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailContent(
    uiState: PostDetailUiState.Success,
    snackbarHostState: SnackbarHostState,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSend: CommentSendCallback
) {
    val postDetail = uiState.postDetail
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        RecheckDialog(
            title = stringResource(id = R.string.are_you_sure_you_want_to_delete),
            onOkClick = onDeleteClick,
            onDismissRequest = { showDeleteDialog = false },
            confirmText = stringResource(id = R.string.delete)
        )
    }

    CommentModalBottomSheet(
        onSend = onSend
    ) { commentModalController ->
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                PostDetailTopAppBar(
                    uiState,
                    onEditClick = onEditClick,
                    onDeleteClick = { showDeleteDialog = true }
                )
            }
        ) { paddingValues ->
            LazyColumn(Modifier.padding(paddingValues)) {
                headerItems(
                    title = postDetail.title,
                    writerName = postDetail.writer.name,
                    date = postDetail.date
                )
                item {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = postDetail.content,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
                item {
                    ThickDivider()
                }
                item {
                    CommentAddButton(
                        onClick = {
                            coroutineScope.launch {
                                commentModalController.show()
                            }
                        }
                    )
                }
                item {
                    PocsDivider()
                }
                commentItems(
                    uiState = uiState.comments,
                    onMoreButtonClick = { },
                    onReplyIconClick = {
                        coroutineScope.launch {
                            commentModalController.show(parentComment = it)
                        }
                    },
                    onCommentClick = {
                        coroutineScope.launch {
                            commentModalController.show(parentComment = it)
                        }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailTopAppBar(
    uiState: PostDetailUiState.Success,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    SmallTopAppBar(
        navigationIcon = {
            AppBarBackButton()
        },
        title = {},
        actions = {
            val options = mutableListOf<Int>()
            if (uiState.canEditPost) {
                options += R.string.edit
            }
            if (uiState.canDeletePost) {
                options += R.string.delete
            }
            if (options.isNotEmpty()) {
                DropdownButton(
                    options = options.map { resourceId ->
                        DropdownOption(
                            label = stringResource(id = resourceId),
                            onClick = {
                                when (resourceId) {
                                    R.string.edit -> onEditClick()
                                    R.string.delete -> onDeleteClick()
                                    else -> throw IllegalArgumentException()
                                }
                            },
                            labelColor = if (resourceId == R.string.delete) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                )
            }
        }
    )
}

private fun LazyListScope.headerItems(title: String, writerName: String, date: String) {
    item {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Box(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    id = R.string.post_subtitle,
                    writerName,
                    date
                ),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            )
        }
    }
    item {
        PocsDivider(startIndent = 20.dp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostDetailFailureContent(uiState: PostDetailUiState.Failure) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {},
                navigationIcon = { AppBarBackButton() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = stringResource(R.string.error_icon),
                modifier = Modifier.size(96.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Box(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.message ?: stringResource(id = R.string.failed_to_load),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun PostDetailContentPreview() {
    val mockComment = CommentItemUiState(
        id = 10,
        parentId = null,
        childrenCount = 0,
        postId = 1,
        isMyComment = true,
        writer = CommentWriterUiState(
            userId = 1,
            name = "홍길동"
        ),
        content = "댓글 내용입니다.",
        date = "오늘"
    )
    val commentsUiState = CommentsUiState.Success(
        comments = listOf(
            mockComment,
            mockComment.copy(childrenCount = 1),
            mockComment.copy(parentId = 10, id = 11),
            mockComment
        )
    )

    PostDetailContent(
        uiState = PostDetailUiState.Success(
            postDetail = PostDetailItemUiState(
                id = 1,
                title = "제목입니다",
                writer = PostWriterUiState(id = 1, name = "김정은"),
                content = "내용이 들어가는 자리입니다.",
                date = "어제",
                category = PostCategory.NOTICE,
            ),
            canDeletePost = true,
            canEditPost = true,
            isDeleteSuccess = false,
            userMessage = null,
            comments = commentsUiState
        ),
        snackbarHostState = SnackbarHostState(),
        onEditClick = {},
        onDeleteClick = {},
        onSend = { _, _ -> }
    )
}

@Preview
@Composable
fun PostDetailFailureContentPreview() {
    PostDetailFailureContent(PostDetailUiState.Failure("연결 실패"))
}