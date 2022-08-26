package com.pocs.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.comment.CommentsUiState
import com.pocs.presentation.model.post.PostDetailUiState
import com.pocs.presentation.model.post.item.PostWriterUiState
import com.pocs.presentation.view.component.bottomsheet.CommentModalController
import com.pocs.presentation.view.component.bottomsheet.MODAL_BOTTOM_SHEET_CONTENT_TAG
import com.pocs.presentation.view.post.detail.PostDetailContent
import com.pocs.test_library.mock.mockComment
import com.pocs.test_library.mock.mockPostDetail1
import com.pocs.test_library.mock.mockReply
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostDetailScreenTest {

    @get:Rule(order = 0)
    val composeRule = createComposeRule()

    private val commentsUiState = CommentsUiState.Success(listOf(mockComment), canAddComment = true)

    private val uiState = PostDetailUiState.Success(
        postDetail = mockPostDetail1.toUiState(),
        canEditPost = true,
        canDeletePost = true,
        comments = commentsUiState
    )

    private lateinit var context: Context

    private fun getString(@StringRes resId: Int) = context.getString(resId)

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

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

            onNodeWithText(mockComment.content).performClick()

            onNodeWithText(getString(R.string.add_reply)).assertIsDisplayed()
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

            onNodeWithContentDescription(getString(R.string.reply_count)).performClick()

            onNodeWithText(getString(R.string.add_reply)).assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowReplyCommentBottomSheet_WhenClickReplyComment() {
        composeRule.run {
            val reply = mockReply

            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        comments = commentsUiState.copy(
                            comments = listOf(reply)
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

            onNodeWithText(reply.content).performClick()

            onNodeWithText(getString(R.string.add_reply)).assertIsDisplayed()
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

            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()

            onNodeWithContentDescription(getString(R.string.comment_text_field)).assertIsDisplayed()
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

            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()
            mainClock.advanceTimeByFrame()

            assertNull(commentModalController.parentId)
            assertNull(commentModalController.commentToBeUpdated)
        }
    }

    @Test
    fun shouldExistControllerParentId_WhenClickComment() {
        composeRule.run {
            val commentModalController = CommentModalController()
            val comment = mockComment

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
            val replyComment = mockReply

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
//            onNode(hasContentDescription(getString(R.string.comment_text_field)) and hasText(comment.content))
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

            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()
            onNodeWithContentDescription(getString(R.string.comment_text_field)).performTextInput("hi")
            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()

            onNodeWithText(getString(R.string.stop)).assertIsDisplayed()
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

            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()
            onNodeWithContentDescription(getString(R.string.comment_text_field)).performTextInput("hi")
            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()
            onNodeWithText(getString(R.string.stop)).performClick()

            onNodeWithContentDescription(getString(R.string.comment_text_field)).assertIsNotDisplayed()
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

            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()
            onNodeWithContentDescription(getString(R.string.comment_text_field)).performTextInput("hi")
            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()
            onNodeWithText(getString(R.string.stop)).performClick()

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
            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()
            onNodeWithContentDescription(getString(R.string.comment_text_field)).performTextInput(
                text
            )
            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()

            onNodeWithText(text).assertIsNotDisplayed()

            onNodeWithText(getString(R.string.keep_writing)).performClick()

            onNodeWithText(text).assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowRecheckDialog_WhenCllickDeleteCommentButton() {
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

            onNodeWithContentDescription(getString(R.string.comment_info_button)).performClick()
            onNodeWithText(getString(R.string.delete)).performClick()

            onNodeWithText(getString(R.string.are_you_sure_you_want_to_delete)).assertIsDisplayed()
        }
    }

    @Test
    fun shouldHideKeyboard_WhenClickSendButton() {
        composeRule.run {
            val controller = CommentModalController()
            val keyboardHelper = KeyboardHelper(composeRule)

            setContent {
                keyboardHelper.view = LocalView.current
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
            onNodeWithContentDescription(getString(R.string.comment_add_button)).performClick()
            onNodeWithContentDescription(getString(R.string.comment_text_field)).performTextInput(
                text
            )
            onNodeWithContentDescription(getString(R.string.send_comment)).performClick()
            mainClock.autoAdvance = false
            mainClock.advanceTimeByFrame() // trigger recomposition
            waitForIdle() // await layout pass to set up animation
            mainClock.advanceTimeByFrame() // give animation a start time
            mainClock.advanceTimeBy(10)

            assertFalse(keyboardHelper.isSoftwareKeyboardShown())
        }
    }

    @Test
    fun shouldShowViews() {
        composeRule.run {
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        postDetail = mockPostDetail1.copy(views = 10).toUiState()
                    ),
                    snackbarHostState = snackbarHostState,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            onNodeWithText("10").assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowTitleInAppBar_WhenScrollUp() {
        composeRule.run {
            val title = "hi"

            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        postDetail = mockPostDetail1.copy(
                            title = title,
                            content = "t\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt\n\nt"
                        ).toUiState()
                    ),
                    snackbarHostState = snackbarHostState,
                    onEditClick = {},
                    onDeleteClick = {},
                    onCommentDelete = {},
                    onCommentCreated = { _, _ -> },
                    onCommentUpdated = { _, _ -> }
                )
            }

            assertEquals(1, findVisibleTexts(text = title).size)

            onRoot().performTouchInput { swipeUp() }

            assertEquals(1, findVisibleTexts(text = title).size)
        }
    }

    @Test
    fun shouldShowDeletedComment_WhenCommentWasDeleted() {
        composeRule.run {
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        comments = commentsUiState.copy(
                            comments = listOf(mockComment.copy(isDeleted = true, childrenCount = 2))
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

            onNodeWithText(getString(R.string.deleted_comment)).assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowAnonymousAsName_WhenNameIsNull() {
        composeRule.run {
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        postDetail = mockPostDetail1.toUiState().copy(
                            writer = PostWriterUiState(
                                name = null,
                                id = 1
                            )
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

            onNodeWithText(getString(R.string.anonymous), substring = true).assertIsDisplayed()
        }
    }

    @Test
    fun shouldNotShowEditButton_WhenCanNotEditComment() {
        composeRule.run {
            val comment = mockComment.copy(
                canDelete = true,
                canEdit = false
            )
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        comments = CommentsUiState.Success(
                            comments = listOf(comment),
                            canAddComment = true
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

            onNodeWithContentDescription(getString(R.string.comment_info_button)).performClick()

            onNodeWithText(getString(R.string.edit)).assertDoesNotExist()
            onNodeWithText(getString(R.string.delete)).assertIsDisplayed()
        }
    }

    @Test
    fun shouldHideOptionModalBottomSheet_WhenClickDeleteButton() {
        composeRule.run {
            val comment = mockComment.copy(canDelete = true)
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        comments = CommentsUiState.Success(
                            comments = listOf(comment),
                            canAddComment = true
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

            onNodeWithContentDescription(getString(R.string.comment_info_button)).performClick()
            onNodeWithText(getString(R.string.delete)).performClick()

            val visibleBottomSheetList = findVisibleNode(hasTestTag(MODAL_BOTTOM_SHEET_CONTENT_TAG))
            assertEquals(0, visibleBottomSheetList.size)
        }
    }

    @Test
    fun shouldDisableCommentAddButton_WhenCanAddCommentIsFalse() {
        composeRule.run {
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        comments = CommentsUiState.Success(
                            comments = emptyList(),
                            canAddComment = false
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

            onNodeWithContentDescription(getString(R.string.comment_add_button)).assertIsNotEnabled()
        }
    }

    @Test
    fun shouldDisableAddingReply_WhenCanAddCommentIsFalse() {
        composeRule.run {
            val comment = mockComment.copy(canDelete = true)
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }

                PostDetailContent(
                    uiState = uiState.copy(
                        comments = CommentsUiState.Success(
                            comments = listOf(comment),
                            canAddComment = false
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

            onNodeWithText(comment.content).assertIsNotEnabled()
        }
    }

    @Suppress("SameParameterValue")
    private fun findVisibleTexts(text: String): List<SemanticsNode> {
        return findVisibleNode(hasText(text))
    }

    @Suppress("SameParameterValue")
    private fun findVisibleNode(matcher: SemanticsMatcher): List<SemanticsNode> {
        return composeRule.onAllNodes(matcher).fetchSemanticsNodes().filter { semanticsNode ->
            val transparentNodes = semanticsNode.layoutInfo.getModifierInfo().filter {
                it.modifier == Modifier.alpha(0.0f)
            }
            transparentNodes.isEmpty()
        }
    }
}