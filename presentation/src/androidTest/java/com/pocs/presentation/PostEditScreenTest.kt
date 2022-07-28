package com.pocs.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pocs.domain.model.PostCategory
import com.pocs.presentation.model.PostEditUiState
import com.pocs.presentation.view.post.edit.PostEditScreen
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

    companion object {
        private const val HOME_STACK_TEXT = "Home"
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun PostEditScreenWithHomeBackStack(uiState: PostEditUiState) {
        val navController = rememberNavController()

        SideEffect {
            navController.navigate("edit")
        }

        NavHost(navController, startDestination = "home") {
            composable(route = "home") {
                Text(HOME_STACK_TEXT)
            }
            composable(route = "edit") {
                PostEditScreen(uiState = uiState, navigateUp = navController::navigateUp)
            }
        }
    }

    @Test
    fun disableSendButton_IfTitleIsEmpty() {
        val fakeUiState = emptyUiState.copy(
            title = "",
            content = "hello",
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState) {}
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
            PostEditScreen(uiState = fakeUiState) {}
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
            PostEditScreen(uiState = fakeUiState) {}
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
            PostEditScreen(uiState = fakeUiState) {}
        }

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("저장하기").assertDoesNotExist()
    }

    @Test
    fun showSnackBar_IfFailedToSavePost() {
        val exception = Exception("fail!!")
        val fakeUiState = emptyUiState.copy(
            title = "title",
            content = "content",
            isInSaving = false,
            onSave = { Result.failure(exception) }
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState) {}
        }

        composeTestRule.onNodeWithContentDescription("저장하기").performClick()

        composeTestRule.onNodeWithText(exception.message!!).assertIsDisplayed()
    }

    @Test
    fun navigateToUp_WhenSuccessSaving() {
        composeTestRule.run {
            setContent {
                PostEditScreenWithHomeBackStack(emptyUiState.copy(title = "a", content = "a"))
            }

            onNodeWithContentDescription("저장하기").performClick()

            onNodeWithText(HOME_STACK_TEXT).assertIsDisplayed()
        }
    }

    @Test
    fun navigateToUp_WhenClickOkButtonOfAlertDialog() {
        composeTestRule.run {
            setContent {
                PostEditScreenWithHomeBackStack(emptyUiState.copy(title = "Hi"))
            }

            onNodeWithContentDescription("뒤로가기").performClick()
            onNodeWithText("정말로 중단할까요?").assertIsDisplayed()

            onNodeWithText("확인").performClick()

            onNodeWithText(HOME_STACK_TEXT).assertIsDisplayed()
        }
    }

    @Test
    fun continueEditing_WhenClickCancelButtonOfAlertDialog() {
        composeTestRule.run {
            setContent {
                PostEditScreenWithHomeBackStack(emptyUiState.copy(title = "Hi"))
            }

            onNodeWithContentDescription("뒤로가기").performClick()
            onNodeWithText("정말로 중단할까요?").assertIsDisplayed()

            onNodeWithText("취소").performClick()

            onNodeWithText(HOME_STACK_TEXT).assertDoesNotExist()
        }
    }

    @Test
    fun shouldNotShowAlertDialog_WhenPressingBackButtonWithEmptyTitleAndContent() {
        composeTestRule.run {
            setContent {
                PostEditScreenWithHomeBackStack(emptyUiState)
            }

            onNodeWithContentDescription("뒤로가기").performClick()

            onNodeWithText("정말로 중단할까요?").assertDoesNotExist()
            onNodeWithText(HOME_STACK_TEXT).assertIsDisplayed()
        }
    }
}