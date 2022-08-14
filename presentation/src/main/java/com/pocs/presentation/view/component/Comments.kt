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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
                CommentFailureContent()
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
                    Comment(
                        uiState = comment,
                        onClick = { onCommentClick(comment) },
                        onReplyIconClick = { onReplyIconClick(comment) },
                        onMoreButtonClick = { onMoreButtonClick(comment) }
                    )
                    PocsDivider()
                }
            }
        }
    }
}

@Composable
fun CommentAddButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                role = Role.Button,
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.add_comment),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun Comment(
    uiState: CommentItemUiState,
    onClick: () -> Unit,
    onReplyIconClick: () -> Unit,
    onMoreButtonClick: () -> Unit
) {
    val isReply = uiState.parentId != null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            )
            .background(
                color = if (isReply) {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.03f)
                } else {
                    MaterialTheme.colorScheme.background
                }
            )
            .padding(start = if (isReply) 20.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp),
            verticalAlignment = Alignment.Top
        ) {
            val onBackgroundColor = MaterialTheme.colorScheme.onBackground

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = uiState.writer.name + stringResource(R.string.middle_dot) + uiState.date,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = onBackgroundColor.copy(alpha = 0.6f)
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
                        tint = onBackgroundColor.copy(alpha = 0.6f),
                        contentDescription = stringResource(R.string.more_info_button)
                    )
                }
            }
        }
        if (isReply) {
            Box(modifier = Modifier.height(16.dp))
        } else {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                CommentLabel(
                    imageVector = Icons.Outlined.Comment,
                    onClick = onReplyIconClick,
                    label = if (uiState.childrenCount == 0) null else uiState.childrenCount.toString(),
                    contentDescription = stringResource(R.string.child_comment_count)
                )
            }
        }
    }
}

@Composable
private fun CommentLabel(
    imageVector: ImageVector,
    label: String?,
    contentDescription: String,
    onClick: () -> Unit
) {
    val color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 20.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        bounded = false,
                        radius = 24.dp
                    ),
                    role = Role.Button,
                ),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = color
        )
        Text(
            modifier = Modifier
                .padding(start = 24.dp)
                .defaultMinSize(minWidth = 32.dp),
            text = label ?: "",
            style = MaterialTheme.typography.labelMedium.copy(color = color)
        )
    }
}

@Composable
private fun CommentFailureContent() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.failed_to_load_comment),
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
        parentId = null,
        childrenCount = 0,
        postId = 1,
        canEdit = true,
        canDelete = true,
        writer = CommentWriterUiState(
            userId = 1,
            name = "홍길동"
        ),
        content = "댓글 내용입니다.",
        date = "오늘"
    )
    val uiState = CommentsUiState.Success(
        comments = listOf(
            mockComment,
            mockComment.copy(childrenCount = 1),
            mockComment.copy(parentId = 10, id = 11),
            mockComment
        )
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
            parentId = null,
            childrenCount = 2,
            postId = 1,
            canEdit = true,
            canDelete = true,
            writer = CommentWriterUiState(
                userId = 1,
                name = "홍길동"
            ),
            content = "댓글 내용입니다.",
            date = "오늘"
        ),
        onClick = {},
        onMoreButtonClick = {},
        onReplyIconClick = {}
    )
}