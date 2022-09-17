package com.pocs.presentation.comment

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.presentation.R
import com.pocs.presentation.model.comment.item.CommentItemUiState
import com.pocs.presentation.view.component.Comment
import com.pocs.test_library.mock.mockComment
import com.pocs.test_library.mock.mockReply
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CommentTest {

    @get:Rule(order = 0)
    val composeRule = createComposeRule()

    private lateinit var context: Context

    private fun getString(@StringRes resId: Int) = context.getString(resId)

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun shouldShowReplyIcon_WhenItIsComment() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockComment,
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}, canAddReply = true
                )
            }

            findReplyLabelButton().assertIsDisplayed()
        }
    }

    @Test
    fun shouldNotShowReplyIcon_WhenItIsReply() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockReply,
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}, canAddReply = true
                )
            }

            findReplyLabelButton().assertDoesNotExist()
        }
    }

    @Test
    fun shouldShowReplyCount_WhenItHasRepliesOverOne() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockComment.copy(childrenCount = 1),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}, canAddReply = true
                )
            }

            onNodeWithText("1").assertIsDisplayed()
        }
    }

    @Test
    fun shouldNotShowReplyCount_WhenItDoesNotHaveReply() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockComment.copy(childrenCount = 0),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}, canAddReply = true
                )
            }

            onNodeWithText("0").assertDoesNotExist()
        }
    }

    @Test
    fun shouldShowMoreInfoButton_WhenCanEdit() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockComment.copy(canEdit = true, canDelete = false),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}, canAddReply = true
                )
            }

            findCommentInfoButton().assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowMoreInfoButton_WhenCanDelete() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockComment.copy(canEdit = false, canDelete = true),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}, canAddReply = true
                )
            }

            findCommentInfoButton().assertIsDisplayed()
        }
    }

    @Test
    fun shouldNotShowMoreInfoButton_WhenCanNotEditAndDelete() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockComment.copy(canEdit = false, canDelete = false),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}, canAddReply = true
                )
            }

            findCommentInfoButton().assertDoesNotExist()
        }
    }

    @Test
    fun shouldDisableClick_WhenCanAddReplyIsFalse() {
        composeRule.run {
            val comment = mockComment
            setContent {
                Comment(
                    canAddReply = false,
                    uiState = comment,
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}
                )
            }

            findCommentContent(comment).assertIsNotEnabled()
        }
    }

    @Test
    fun shouldDisableReplyIconClick_WhenCanAddReplyIsFalse() {
        composeRule.run {
            setContent {
                Comment(
                    canAddReply = false,
                    uiState = mockComment,
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}
                )
            }

            findReplyLabelButton().assertIsNotEnabled()
        }
    }

    private fun findCommentContent(comment: CommentItemUiState): SemanticsNodeInteraction {
        return composeRule.onNodeWithText(comment.content)
    }

    private fun findReplyLabelButton(): SemanticsNodeInteraction {
        return composeRule.onNodeWithContentDescription(getString(R.string.reply_count))
    }

    private fun findCommentInfoButton(): SemanticsNodeInteraction {
        return composeRule.onNodeWithContentDescription(getString(R.string.comment_info_button))
    }
}
