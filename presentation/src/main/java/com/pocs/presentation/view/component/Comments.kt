package com.pocs.presentation.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.comment.CommentsUiState
import com.pocs.presentation.model.comment.item.CommentItemUiState
import com.pocs.presentation.model.comment.item.CommentWriterUiState

typealias CommentCallback = (CommentItemUiState) -> Unit

fun LazyListScope.commentItems(
    uiState: CommentsUiState,
    onCommentClick: CommentCallback,
    onReplyIconClick: CommentCallback,
    onMoreButtonClick: CommentCallback
) {
    when (uiState) {
        is CommentsUiState.Failure -> {
            item {
                CommentFailureContent(
                    uiState.message ?: stringResource(R.string.failed_to_load_comment)
                )
            }
        }
        CommentsUiState.Loading -> {
            item {
                LoadingContent(modifier = Modifier.padding(20.dp))
            }
        }
        is CommentsUiState.Success -> {
            items(count = uiState.comments.size) { index ->
                val comment = uiState.comments[index]

                Column {
                    if (comment.isDeleted) {
                        DeletedComment()
                    } else {
                        Comment(
                            uiState = comment,
                            canAddReply = uiState.canAddComment,
                            onClick = { onCommentClick(comment) },
                            onReplyIconClick = { onReplyIconClick(comment) },
                            onMoreButtonClick = { onMoreButtonClick(comment) }
                        )
                    }
                    PocsDivider()
                }
            }
        }
    }
}

@Composable
fun CommentAddButton(enabled: Boolean, onClick: () -> Unit) {
    val commentAddButtonDescription = stringResource(id = R.string.comment_add_button)
    val labelResource = if (enabled) R.string.add_comment else R.string.can_add_comment_only_member

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                enabled = enabled,
                role = Role.Button,
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .semantics {
                contentDescription = commentAddButtonDescription
            }
    ) {
        Text(
            text = stringResource(id = labelResource),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun Comment(
    uiState: CommentItemUiState,
    canAddReply: Boolean,
    onClick: () -> Unit,
    onReplyIconClick: () -> Unit,
    onMoreButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                enabled = canAddReply,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            )
            .background(
                color = if (uiState.isReply) {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.03f)
                } else {
                    MaterialTheme.colorScheme.background
                }
            )
            .padding(start = if (uiState.isReply) 20.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp),
            verticalAlignment = Alignment.Top
        ) {
            val onBackgroundColor = MaterialTheme.colorScheme.onBackground
            val name = uiState.writer.name ?: stringResource(id = R.string.anonymous)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name + stringResource(R.string.middle_dot) + uiState.date,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = onBackgroundColor.copy(alpha = 0.5f)
                    )
                )
                Box(Modifier.height(8.dp))
                Text(
                    text = uiState.content,
                    style = MaterialTheme.typography.bodyMedium.copy(color = onBackgroundColor)
                )
            }
            if (uiState.showMoreInfoButton) {
                IconButton(onClick = onMoreButtonClick) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.MoreVert,
                        tint = onBackgroundColor.copy(alpha = 0.5f),
                        contentDescription = stringResource(R.string.comment_info_button)
                    )
                }
            }
        }
        if (uiState.isReply) {
            Box(modifier = Modifier.height(16.dp))
        } else {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Label(
                    imageVector = Icons.Outlined.Comment,
                    onClick = if (canAddReply) onReplyIconClick else null,
                    label = if (uiState.childrenCount == 0) {
                        null
                    } else {
                        uiState.childrenCount.toString()
                    },
                    contentDescription = stringResource(R.string.reply_count)
                )
            }
        }
    }
}

@Composable
private fun DeletedComment() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        text = stringResource(R.string.deleted_comment),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
    )
}

@Composable
private fun CommentFailureContent(errorMessage: String) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommentsPreview() {
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
    val uiState = CommentsUiState.Success(
        comments = listOf(
            mockComment.copy(isDeleted = true),
            mockComment.copy(childrenCount = 1),
            mockComment.copy(parentId = 10, id = 11),
            mockComment
        ),
        canAddComment = true
    )

    LazyColumn {
        commentItems(
            uiState = uiState,
            onCommentClick = {},
            onReplyIconClick = {},
            onMoreButtonClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommentPreview() {
    Comment(
        uiState = CommentItemUiState(
            id = 10,
            parentId = 10,
            childrenCount = 2,
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
        ),
        onClick = {},
        onMoreButtonClick = {},
        onReplyIconClick = {},
        canAddReply = true
    )
}

@Preview(showBackground = true)
@Composable
fun RemovedCommentPreview() {
    DeletedComment()
}
