package com.pocs.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.comment.CommentsUiState
import com.pocs.presentation.model.post.PostDetailUiState
import com.pocs.presentation.view.component.bottomsheet.CommentModalController
import com.pocs.presentation.view.component.bottomsheet.commentTextFieldDescription
import com.pocs.presentation.view.component.commentAddButtonDescription
import com.pocs.presentation.view.post.detail.PostDetailContent
import com.pocs.test_library.mock.mockCommentItemUiState
import com.pocs.test_library.mock.mockPostDetail1
import org.junit.Assert.*
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

    @Test
    fun shouldControllerValuesAreNull_WhenClickAddCommentButton() {
        composeRule.run {
            val commentModalController = CommentModalController()

            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState,
                    snackbarHostState = snackbarHostState,
                    commentModalController = commentModalController,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            assertNull(commentModalController.parentId)
            assertNull(commentModalController.commentToBeUpdated)

            onNodeWithContentDescription(commentAddButtonDescription).performClick()
            mainClock.advanceTimeByFrame()

            assertNull(commentModalController.parentId)
            assertNull(commentModalController.commentToBeUpdated)
        }
    }

    @Test
    fun shouldExistControllerParentId_WhenClickComment() {
        composeRule.run {
            val commentModalController = CommentModalController()
            val comment = commentItem.copy(parentId = null)

            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    commentModalController = commentModalController,
                    uiState = uiState.copy(
                        comments = commentsUiState.copy(comments = listOf(comment))
                    ),
                    snackbarHostState = snackbarHostState,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            assertNull(commentModalController.parentId)

            onNodeWithText(comment.content).performClick()

            assertEquals(comment.id, commentModalController.parentId)
        }
    }

    @Test
    fun replyParentIdIsSameWithControllerParentId_WhenClickReplyComment() {
        composeRule.run {
            val commentModalController = CommentModalController()
            val parentId = 23
            val replyComment = commentItem.copy(parentId = parentId)

            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    commentModalController = commentModalController,
                    uiState = uiState.copy(
                        comments = commentsUiState.copy(comments = listOf(replyComment))
                    ),
                    snackbarHostState = snackbarHostState,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            assertNull(commentModalController.parentId)

            onNodeWithText(replyComment.content).performClick()

            assertEquals(replyComment.parentId, commentModalController.parentId)
        }
    }

//    아래의 테스트는 https://issuetracker.google.com/issues/229378536 문제로 인해 잠시 보류한다.
//    발생하는 문제는 텍스트 셀렉션이 out of bounds 하는 것이다.
//
//    @Test
//    fun commentTextFieldHasText_WhenClickEditComment() {
//        composeRule.run {
//            val commentModalController = CommentModalController()
//            val comment = commentItem.copy(parentId = null, canEdit = true, content = "abc")
//
//            setContent {
//                val snackbarHostState = remember { SnackbarHostState() }
//
//                PostDetailContent(
//                    commentModalController = commentModalController,
//                    uiState = uiState.copy(
//                        comments = commentsUiState.copy(comments = listOf(comment))
//                    ),
//                    snackbarHostState = snackbarHostState,
//                    onEditClick = {},
//                    onDeleteClick = {},
//                    onCommentDelete = {},
//                    onCommentCreated = { _, _ -> },
//                    onCommentUpdated = { _, _ -> }
//                )
//            }
//
//            assertNull(commentModalController.commentToBeUpdated)
//
//            onNodeWithContentDescription("댓글 정보 버튼").performClick()
//            onNodeWithText("편집").performClick()
//
//            onNode(hasContentDescription(commentTextFieldDescription) and hasText(comment.content))
//                .assertIsDisplayed()
//        }
//    }

    @Test
    fun shouldShowRecheckDialog_WhenClickOutOfCommentBottomSheet() {
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
            onNodeWithContentDescription(commentTextFieldDescription).performTextInput("hi")
            onNodeWithContentDescription(commentAddButtonDescription).performClick()

            onNodeWithText("그만두기").assertIsDisplayed()
        }
    }

    @Test
    fun shouldHideCommentBottomSheet_WhenClickStopButtonOfRecheckDialog() {
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
            onNodeWithContentDescription(commentTextFieldDescription).performTextInput("hi")
            onNodeWithContentDescription(commentAddButtonDescription).performClick()
            onNodeWithText("그만두기").performClick()

            onNodeWithContentDescription(commentTextFieldDescription).assertIsNotDisplayed()
        }
    }

    @Test
    fun shouldClearController_WhenStoppingAddComment() {
        composeRule.run {
            val controller = CommentModalController()

            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    commentModalController = controller,
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
            onNodeWithContentDescription(commentTextFieldDescription).performTextInput("hi")
            onNodeWithContentDescription(commentAddButtonDescription).performClick()
            onNodeWithText("그만두기").performClick()

            assertTrue(controller.isCleared)
        }
    }

    @Test
    fun shouldShowBottomSheet_WhenClickContinueButtonOfRecheckDialog() {
        composeRule.run {
            val controller = CommentModalController()

            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    commentModalController = controller,
                    uiState = uiState,
                    snackbarHostState = snackbarHostState,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            val text = "hello"
            onNodeWithContentDescription(commentAddButtonDescription).performClick()
            onNodeWithContentDescription(commentTextFieldDescription).performTextInput(text)
            onNodeWithContentDescription(commentAddButtonDescription).performClick()

            onNodeWithText(text).assertIsNotDisplayed()

            onNodeWithText("계속작성").performClick()

            onNodeWithText(text).assertIsDisplayed()
        }
    }
}