package com.pocs.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.comment.CommentsUiState
import com.pocs.presentation.model.post.PostDetailUiState
import com.pocs.presentation.view.component.bottomsheet.commentTextFieldDescription
import com.pocs.presentation.view.component.commentAddButtonDescription
import com.pocs.presentation.view.post.detail.PostDetailContent
import com.pocs.test_library.mock.mockCommentItemUiState
import com.pocs.test_library.mock.mockPostDetail1
import org.junit.Rule
import org.junit.Test

class PostDetailScreenTest {

    @get:Rule(order = 0)
    val composeRule = createComposeRule()

    private val commentItem = mockCommentItemUiState

    private val commentsUiState = CommentsUiState.Success(listOf(commentItem))

    private val uiState = PostDetailUiState.Success(
        postDetail = mockPostDetail1.toUiState(),
        canEditPost = true,
        canDeletePost = true,
        comments = commentsUiState
    )

    @Test
    fun shouldShowReplyCommentBottomSheet_WhenClickComment() {
        composeRule.run {
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState,
                    snackbarHostState = snackbarHostState,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            onNodeWithText(commentItem.content).performClick()

            onNodeWithText("답글 추가…").assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowReplyCommentBottomSheet_WhenClickReplyIcon() {
        composeRule.run {
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState,
                    snackbarHostState = snackbarHostState,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            onNodeWithContentDescription("대댓글 갯수").performClick()

            onNodeWithText("답글 추가…").assertIsDisplayed()
        }
    }


    @Test
    fun shouldShowReplyCommentBottomSheet_WhenClickReplyComment() {
        composeRule.run {
            val comment = commentItem.copy(parentId = 2)

            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        comments = commentsUiState.copy(
                            comments = listOf(comment)
                        )
                    ),
                    snackbarHostState = snackbarHostState,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            onNodeWithText(comment.content).performClick()

            onNodeWithText("답글 추가…").assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowCommentBottomSheet_WhenClickAddCommentButton() {
        composeRule.run {
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState,
                    snackbarHostState = snackbarHostState,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            onNodeWithContentDescription(commentAddButtonDescription).performClick()

            onNodeWithContentDescription(commentTextFieldDescription).assertIsDisplayed()
        }
    }
}