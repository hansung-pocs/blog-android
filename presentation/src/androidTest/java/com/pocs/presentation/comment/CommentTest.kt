package com.pocs.presentation.comment

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.pocs.presentation.view.component.Comment
import com.pocs.test_library.mock.mockCommentItemUiState
import org.junit.Rule
import org.junit.Test

class CommentTest {

    @get:Rule(order = 0)
    val composeRule = createComposeRule()

    @Test
    fun shouldShowReplyIcon_WhenItIsComment() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockCommentItemUiState.copy(parentId = null),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}
                )
            }

            onNodeWithContentDescription("대댓글 갯수").assertIsDisplayed()
        }
    }

    @Test
    fun shouldNotShowReplyIcon_WhenItIsReply() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockCommentItemUiState.copy(parentId = 2),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}
                )
            }

            onNodeWithContentDescription("대댓글 갯수").assertDoesNotExist()
        }
    }

    @Test
    fun shouldShowReplyCount_WhenItHasRepliesOverOne() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockCommentItemUiState.copy(parentId = null, childrenCount = 1),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}
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
                    uiState = mockCommentItemUiState.copy(parentId = null, childrenCount = 0),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}
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
                    uiState = mockCommentItemUiState.copy(canEdit = true, canDelete = false),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}
                )
            }

            onNodeWithContentDescription("더보기 버튼").assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowMoreInfoButton_WhenCanDelete() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockCommentItemUiState.copy(canEdit = false, canDelete = true),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}
                )
            }

            onNodeWithContentDescription("더보기 버튼").assertIsDisplayed()
        }
    }

    @Test
    fun shouldNotShowMoreInfoButton_WhenCanNotEditAndDelete() {
        composeRule.run {
            setContent {
                Comment(
                    uiState = mockCommentItemUiState.copy(canEdit = false, canDelete = false),
                    onClick = {}, onMoreButtonClick = {}, onReplyIconClick = {}
                )
            }

            onNodeWithContentDescription("더보기 버튼").assertDoesNotExist()
        }
    }
}