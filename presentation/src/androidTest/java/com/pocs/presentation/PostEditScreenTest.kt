package com.pocs.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.pocs.presentation.model.PostEditUiState
import com.pocs.presentation.view.post.edit.PostEditScreen
import org.junit.Rule
import org.junit.Test

class PostEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun disableSendButton_IfTitleIsEmpty() {
        val fakeUiState = PostEditUiState(
            title = "",
            content = "hello",
            onChangeTitle = {},
            onChangeContent = {},
            onSave = { Result.success(true) }
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithContentDescription("저장하기").assertIsNotEnabled()
    }

    @Test
    fun disableSendButton_IfContentIsEmpty() {
        val fakeUiState = PostEditUiState(
            title = "title",
            content = "",
            onChangeTitle = {},
            onChangeContent = {},
            onSave = { Result.success(true) }
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithContentDescription("저장하기").assertIsNotEnabled()
    }

    @Test
    fun enableSendButton_IfTitleAndContentAreNotEmpty() {
        val fakeUiState = PostEditUiState(
            title = "title",
            content = "content",
            onChangeTitle = {},
            onChangeContent = {},
            onSave = { Result.success(true) }
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithContentDescription("저장하기").assertIsEnabled()
    }

    @Test
    fun showCircularProgressIndicator_IfUiStateIsInSaving() {
        val fakeUiState = PostEditUiState(
            title = "title",
            content = "content",
            isInSaving = true,
            onChangeTitle = {},
            onChangeContent = {},
            onSave = { Result.success(true) }
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("저장하기").assertDoesNotExist()
    }

    @Test
    fun showSnackBar_IfFailedToSavePost() {
        val exception = Exception("fail!!")
        val fakeUiState = PostEditUiState(
            title = "title",
            content = "content",
            isInSaving = false,
            onChangeTitle = {},
            onChangeContent = {},
            onSave = { Result.failure(exception) }
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithContentDescription("저장하기").performClick()

        composeTestRule.onNodeWithText(exception.message!!).assertIsDisplayed()
    }
}