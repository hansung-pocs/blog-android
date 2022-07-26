package com.pocs.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.pocs.domain.model.PostCategory
import com.pocs.presentation.model.PostEditUiState
import com.pocs.presentation.view.post.edit.PostEditScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PostEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val emptyUiState = PostEditUiState(
        id = 1,
        title = "",
        content = "",
        category = PostCategory.NOTICE,
        onTitleChange = {},
        onContentChange = {},
        onSave = { Result.success(true) }
    )

    @Test
    fun disableSendButton_IfTitleIsEmpty() {
        val fakeUiState = emptyUiState.copy(
            title = "",
            content = "hello",
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithContentDescription("저장하기").assertIsNotEnabled()
    }

    @Test
    fun disableSendButton_IfContentIsEmpty() {
        val fakeUiState = emptyUiState.copy(
            title = "title",
            content = "",
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithContentDescription("저장하기").assertIsNotEnabled()
    }

    @Test
    fun enableSendButton_IfTitleAndContentAreNotEmpty() {
        val fakeUiState = emptyUiState.copy(
            title = "title",
            content = "content",
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithContentDescription("저장하기").assertIsEnabled()
    }

    @Test
    fun showCircularProgressIndicator_IfUiStateIsInSaving() {
        val fakeUiState = emptyUiState.copy(
            title = "title",
            content = "content",
            isInSaving = true,
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("저장하기").assertDoesNotExist()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun showSnackBar_IfFailedToSavePost() = runTest {
        val exception = Exception("fail!!")
        val fakeUiState = emptyUiState.copy(
            title = "title",
            content = "content",
            isInSaving = false,
            onSave = { Result.failure(exception) }
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState)
        }

        composeTestRule.onNodeWithContentDescription("저장하기").performClick()

        // "SnackBar"가 보이는 시간으로 앞당긴다.
        composeTestRule.mainClock.advanceTimeBy(50)
        composeTestRule.onNodeWithText(exception.message!!).assertIsDisplayed()
    }
}