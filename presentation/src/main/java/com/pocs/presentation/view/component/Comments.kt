package com.pocs.presentation.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pocs.presentation.R
import com.pocs.presentation.model.comment.item.CommentItemUiState
import com.pocs.presentation.model.comment.item.CommentWriterUiState

typealias CommentCallback = (CommentItemUiState) -> Unit

@Composable
fun Comments(
    modifier: Modifier = Modifier,
    comments: List<CommentItemUiState>,
    onCommentClick: CommentCallback,
    onSubCommentIconClick: CommentCallback,
    onMoreButtonClick: CommentCallback
) {
    LazyColumn(modifier = modifier) {
        items(count = comments.size) { index ->
            val comment = comments[index]

            Column {
                Comment(
                    uiState = comment,
                    onClick = { onCommentClick(comment) },
                    onSubCommentIconClick = { onSubCommentIconClick(comment) },
                    onMoreButtonClick = { onMoreButtonClick(comment) }
                )
            }
        }
    }
}

@Composable
private fun Comment(
    uiState: CommentItemUiState,
    onClick: () -> Unit,
    onSubCommentIconClick: () -> Unit,
    onMoreButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Row(
            modifier = Modifier.padding(top = 16.dp, start = 20.dp),
            verticalAlignment = Alignment.Top
        ) {
            val onBackgroundColor = MaterialTheme.colorScheme.onBackground

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = uiState.writer.name + stringResource(R.string.middle_dot) + uiState.time,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = onBackgroundColor.copy(alpha = 0.6f)
                    )
                )
                Box(Modifier.height(8.dp))
                Text(
                    text = uiState.content,
                    style = MaterialTheme.typography.titleMedium.copy(color = onBackgroundColor)
                )
            }
            if (uiState.isMyComment) {
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
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            CommentLabel(
                imageVector = Icons.Outlined.Comment,
                onClick = onSubCommentIconClick,
                label = uiState.childrenCount.toString(),
                contentDescription = stringResource(R.string.child_comment_count)
            )
        }
        PocsDivider()
    }
}

@Composable
private fun CommentLabel(
    imageVector: ImageVector,
    label: String,
    contentDescription: String,
    onClick: () -> Unit
) {
    val color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)

    IconButton(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = color
            )
            Box(modifier = Modifier.width(4.dp))
            Text(
                text = label, style = MaterialTheme.typography.labelMedium.copy(color = color)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentsPreview() {
    val mockComment = CommentItemUiState(
        id = 10,
        parentId = null,
        childrenCount = 2,
        postId = 1,
        isMyComment = true,
        writer = CommentWriterUiState(
            userId = 1,
            name = "홍길동"
        ),
        content = "댓글 내용입니다.",
        createdAt = "오늘",
        updatedAt = null
    )
    Comments(
        comments = listOf(mockComment, mockComment, mockComment, mockComment),
        onCommentClick = {},
        onSubCommentIconClick = {},
        onMoreButtonClick = {}
    )
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
            isMyComment = true,
            writer = CommentWriterUiState(
                userId = 1,
                name = "홍길동"
            ),
            content = "댓글 내용입니다.",
            createdAt = "오늘",
            updatedAt = "오늘"
        ),
        onClick = {},
        onMoreButtonClick = {},
        onSubCommentIconClick = {}
    )
}