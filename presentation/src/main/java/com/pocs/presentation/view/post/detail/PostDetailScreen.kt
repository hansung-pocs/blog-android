package com.pocs.presentation.view.post.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.pocs.presentation.view.component.bottomsheet.*
import com.pocs.presentation.view.component.button.AppBarBackButton
import com.pocs.presentation.view.component.button.DropdownButton
import com.pocs.presentation.view.component.button.DropdownOption
import com.pocs.presentation.view.component.markdown.MarkdownText
import kotlinx.coroutines.launch

private const val HEADER_KEY = "header"

@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel,
    onEditClick: () -> Unit,
    onDeleteSuccess: () -> Unit,
    onUserNameClick: (userId: Int) -> Unit
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

            var message = uiState.userMessage
            if (message == null && uiState.userMessageRes != null) {
                message = stringResource(id = uiState.userMessageRes)
            }
            if (message != null) {
                LaunchedEffect(message) {
                    snackbarHostState.showSnackbar(message)
                    viewModel.userMessageShown()
                }
            }

            PostDetailContent(
                uiState = uiState,
                snackbarHostState = snackbarHostState,
                onEditClick = onEditClick,
                onDeleteClick = { viewModel.requestPostDeleting(uiState.postDetail.id) },
                onUserNameClick = onUserNameClick,
                onCommentDelete = viewModel::deleteComment,
                onCommentCreated = viewModel::addComment,
                onCommentUpdated = viewModel::updateComment
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailContent(
    uiState: PostDetailUiState.Success,
    commentModalController: CommentModalController = remember { CommentModalController() },
    optionModalController: OptionModalController<CommentItemUiState> = remember {
        OptionModalController()
    },
    snackbarHostState: SnackbarHostState,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onUserNameClick: (userId: Int) -> Unit,
    onCommentDelete: (commentId: Int) -> Unit,
    onCommentCreated: CommentCreateCallback,
    onCommentUpdated: CommentUpdateCallback
) {
    val postDetail = uiState.postDetail
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var commentToBeDeleted by remember { mutableStateOf<CommentItemUiState?>(null) }

    if (showDeleteDialog) {
        RecheckDialog(
            title = stringResource(id = R.string.are_you_sure_you_want_to_delete),
            onOkClick = onDeleteClick,
            onDismissRequest = { showDeleteDialog = false },
            confirmText = stringResource(id = R.string.delete)
        )
    }

    if (commentToBeDeleted != null) {
        RecheckDialog(
            title = stringResource(id = R.string.are_you_sure_you_want_to_delete),
            onOkClick = {
                commentToBeDeleted?.let { onCommentDelete(it.id) }
                commentToBeDeleted = null
            },
            onDismissRequest = { commentToBeDeleted = null },
            confirmText = stringResource(id = R.string.delete)
        )
    }

    CommentModalBottomSheet(
        controller = commentModalController,
        onCreated = onCommentCreated,
        onUpdated = onCommentUpdated
    ) {
        OptionModalBottomSheet(
            controller = optionModalController,
            optionBuilder = { comment ->
                val options = mutableListOf<Option<CommentItemUiState>>()
                if (comment?.canEdit == true) {
                    options.add(
                        Option(
                            imageVector = Icons.Default.Edit,
                            stringResId = R.string.edit,
                            onClick = {
                                coroutineScope.launch {
                                    commentModalController.showForUpdate(it)
                                }
                            }
                        )
                    )
                }
                if (comment?.canDelete == true) {
                    options.add(
                        Option(
                            imageVector = Icons.Default.Delete,
                            stringResId = R.string.delete,
                            onClick = { commentToBeDeleted = it }
                        )
                    )
                }
                return@OptionModalBottomSheet options
            }
        ) {
            val lazyListState = rememberLazyListState()

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    PostDetailTopAppBar(
                        uiState,
                        lazyListState = lazyListState,
                        onEditClick = onEditClick,
                        onDeleteClick = { showDeleteDialog = true }
                    )
                }
            ) { paddingValues ->
                val anonymousString = stringResource(id = R.string.anonymous)
                val middleDot = stringResource(id = R.string.middle_dot)

                LazyColumn(Modifier.padding(paddingValues), state = lazyListState) {
                    headerItems(
                        title = postDetail.title,
                        writerName = postDetail.writer.name ?: anonymousString,
                        subtitleDivider = middleDot,
                        date = postDetail.date,
                        onlyMember = postDetail.onlyMember,
                        onWriterNameClick = { onUserNameClick(postDetail.writer.id) }
                    )
                    item {
                        MarkdownText(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            markdown = postDetail.content,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                    postInfoItems(views = uiState.postDetail.views)
                    item {
                        ThickDivider()
                    }
                    if (uiState.comments is CommentsUiState.Success) {
                        item {
                            CommentAddButton(
                                enabled = uiState.comments.canAddComment,
                                onClick = {
                                    coroutineScope.launch {
                                        commentModalController.showForCreate()
                                    }
                                }
                            )
                        }
                        item {
                            PocsDivider()
                        }
                    }
                    commentItems(
                        uiState = uiState.comments,
                        onMoreButtonClick = {
                            coroutineScope.launch {
                                optionModalController.show(onClickCallBackData = it)
                            }
                        },
                        onReplyIconClick = {
                            coroutineScope.launch {
                                commentModalController.showForCreate(parentComment = it)
                            }
                        },
                        onWriterNameClick = onUserNameClick,
                        onCommentClick = {
                            coroutineScope.launch {
                                commentModalController.showForCreate(parentComment = it)
                            }
                        },
                    )
                    item {
                        Box(Modifier.height(104.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailTopAppBar(
    uiState: PostDetailUiState.Success,
    lazyListState: LazyListState,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val titleAlpha = rememberTitleAlphaFromScrollOffset(
        key = HEADER_KEY,
        lazyListState = lazyListState
    )

    SmallTopAppBar(
        navigationIcon = {
            AppBarBackButton()
        },
        title = {
            Text(
                text = uiState.postDetail.title,
                modifier = Modifier.alpha(titleAlpha.value),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
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

private const val USER_NAME_TAG = "userName"

private fun LazyListScope.headerItems(
    title: String,
    writerName: String,
    subtitleDivider: String,
    date: String,
    onlyMember: Boolean,
    onWriterNameClick: () -> Unit
) {
    val annotatedInfoText = buildAnnotatedString {
        pushStringAnnotation(tag = USER_NAME_TAG, annotation = "")
        append(writerName)
        append(subtitleDivider)
        pop()
        append(date)
    }

    item(HEADER_KEY) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            )
            Box(modifier = Modifier.height(8.dp))
            ClickableText(
                text = annotatedInfoText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
                onClick = { offset ->
                    annotatedInfoText.getStringAnnotations(
                        tag = USER_NAME_TAG,
                        offset,
                        offset
                    ).firstOrNull()?.let {
                        onWriterNameClick()
                    }
                },
            )
            if (onlyMember) {
                Box(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.can_see_only_member),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
    item {
        PocsDivider(startIndent = 20.dp)
    }
}

private fun LazyListScope.postInfoItems(views: Int) {
    item {
        Row(Modifier.padding(horizontal = 4.dp)) {
            Label(
                imageVector = Icons.Default.Visibility,
                label = views.toString(),
                contentDescription = stringResource(id = R.string.views)
            )
        }
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
        FailureContent(
            modifier = Modifier.padding(it),
            exception = uiState.exception
        )
    }
}

@Preview
@Composable
private fun PostDetailContentPreview() {
    val mockComment = CommentItemUiState(
        id = 10,
        parentId = 10,
        childrenCount = 0,
        postId = 1,
        canEdit = true,
        canDelete = true,
        writer = CommentWriterUiState(
            userId = 1,
            name = "홍길동"
        ),
        content = "댓글 내용입니다.",
        date = "오늘",
        isDeleted = false
    )
    val commentsUiState = CommentsUiState.Success(
        comments = listOf(
            mockComment,
            mockComment.copy(childrenCount = 1),
            mockComment.copy(parentId = 10, id = 11),
            mockComment
        ),
        canAddComment = true
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
                views = 10,
                onlyMember = true
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
        onUserNameClick = {},
        onCommentDelete = {},
        onCommentCreated = { _, _ -> },
        onCommentUpdated = { _, _ -> }
    )
}

@Preview
@Composable
fun PostDetailFailureContentPreview() {
    PostDetailFailureContent(PostDetailUiState.Failure(Exception("연결 실패")))
}
