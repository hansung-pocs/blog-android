package com.pocs.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.constant.MAX_POST_TITLE_LEN
import com.pocs.presentation.model.post.PostEditUiState
import com.pocs.presentation.view.post.edit.PostEditScreen
import com.pocs.presentation.extension.assertSnackBarIsDisplayed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PostEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val emptyUiState = PostEditUiState(
        postId = 1,
        title = "",
        content = TextFieldValue(),
        category = PostCategory.NOTICE,
        currentUserType = UserType.ADMIN,
        onlyMember = true,
        onTitleChange = {},
        onContentChange = {},
        onCategoryChange = {},
        onSave = { Result.success(Unit) },
        onOnlyMemberChange = {}
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
                PostEditScreen(uiState = uiState, navigateUp = navController::navigateUp) {}
            }
        }
    }

    @Test
    fun disableSendButton_IfTitleIsEmpty() {
        val fakeUiState = emptyUiState.copy(
            title = "",
            content = TextFieldValue("hello"),
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState, {}) {}
        }

        composeTestRule.onNodeWithContentDescription("저장하기").assertIsNotEnabled()
    }

    @Test
    fun disableSendButton_IfContentIsEmpty() {
        val fakeUiState = emptyUiState.copy(
            title = "title",
            content = TextFieldValue(""),
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState, {}) {}
        }

        composeTestRule.onNodeWithContentDescription("저장하기").assertIsNotEnabled()
    }

    @Test
    fun enableSendButton_IfTitleAndContentAreNotEmpty() {
        val fakeUiState = emptyUiState.copy(
            title = "title",
            content = TextFieldValue("content"),
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState, {}) {}
        }

        composeTestRule.onNodeWithContentDescription("저장하기").assertIsEnabled()
    }

    @Test
    fun showCircularProgressIndicator_IfUiStateIsInSaving() {
        val fakeUiState = emptyUiState.copy(
            title = "title",
            content = TextFieldValue("content"),
            isInSaving = true,
        )
        composeTestRule.setContent {
            PostEditScreen(uiState = fakeUiState, {}) {}
        }

        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("저장하기").assertDoesNotExist()
    }

    @Test
    fun showSnackBar_IfFailedToSavePost() {
        val exception = Exception("fail!!")
        val fakeUiState = emptyUiState.copy(
            title = "title",
            content = TextFieldValue("content"),
            isInSaving = false,
            onSave = { Result.failure(exception) }
        )
        composeTestRule.run {
            setContent {
                PostEditScreen(uiState = fakeUiState, {}) {}
            }

            assertSnackBarIsDisplayed(
                before = {
                    onNodeWithContentDescription("저장하기").performClick()
                },
                message = exception.message!!
            )
        }
    }

    @Test
    fun navigateToUp_WhenSuccessSaving() {
        composeTestRule.run {
            setContent {
                PostEditScreenWithHomeBackStack(
                    emptyUiState.copy(
                        title = "a",
                        content = TextFieldValue("a")
                    )
                )
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

    @Test
    fun shouldNotInputEnterCharacter_AtTitleTextField() {
        composeTestRule.setContent {
            PostEditScreen(uiState = emptyUiState.copy(
                onTitleChange = {
                    assertEquals("hello", it)
                }
            ), {}) {}
        }

        composeTestRule.onNodeWithContentDescription("제목 입력창").performTextInput("he\nll\no\n")
    }

    @Test
    fun shouldLimitLengthOfTitle_WhenTypeTitle() {
        composeTestRule.setContent {
            PostEditScreen(uiState = emptyUiState.copy(onTitleChange = {
                assertTrue(it.length <= MAX_POST_TITLE_LEN)
            }), {}) {}
        }

        val stringBuilder = StringBuilder()
        for (i in 1..(MAX_POST_TITLE_LEN - 2)) {
            stringBuilder.append("가")
        }

        for (i in 1..4) {
            stringBuilder.append("가")
            composeTestRule.onNodeWithContentDescription("제목 입력창")
                .performTextInput(stringBuilder.toString())
        }
    }
}