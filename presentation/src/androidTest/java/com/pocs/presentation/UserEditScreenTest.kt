package com.pocs.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.pocs.presentation.constant.MAX_USER_NAME_LEN
import com.pocs.presentation.model.user.UserEditUiState
import com.pocs.presentation.view.user.edit.UserEditContent
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UserEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var mockUiState by mutableStateOf(UserEditUiState(
        1,
        "박민석",
        "hello@gmiad.com",
        "google",
        "https://github.com/",
        isInSaving = false,
        onUpdate = ::onUpdate,
        onSave = { Result.success(Unit) }
    ))

    private fun onUpdate(userEditUiState: UserEditUiState) {
        mockUiState = userEditUiState
    }


    @Test
    fun showRecheckDialog_WhenUserClickBackButton() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState,
                    navigateUp = {},
                    onSuccessToSave = {}
                )
            }

            onNodeWithContentDescription("뒤로가기").performClick()

            onNodeWithText("정말로 중단할까요?").assertIsDisplayed()
        }
    }

    @Test
    fun removeTextFieldValues_WhenClickClearButton() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState,
                    navigateUp = {},
                    onSuccessToSave = {}
                )
            }

            val name = mockUiState.name
            onNodeWithText(name).assertIsDisplayed()

            onAllNodesWithContentDescription("입력 창 내용 지우기")[0].performClick()

            onNodeWithText(name).assertDoesNotExist()
        }
    }

    @Test
    fun showPasswordDialog_WhenClickSendButton() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState,
                    navigateUp = {},
                    onSuccessToSave = {}
                )
            }

            onNodeWithContentDescription("저장하기").performClick()

            onNodeWithText("비밀번호를 입력하세요").assertIsDisplayed()
        }
    }

    @Test
    fun disableSaveButton_WhenNameIsEmpty() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(name = "", email = "abc"),
                    navigateUp = {},
                    onSuccessToSave = {}
                )
            }

            onNodeWithContentDescription("저장하기").assertIsNotEnabled()
        }
    }

    @Test
    fun disableSaveButton_WhenEmailIsEmpty() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(name = "kim", email = ""),
                    navigateUp = {},
                    onSuccessToSave = {}
                )
            }

            onNodeWithContentDescription("저장하기").assertIsNotEnabled()
        }
    }

    @Test
    fun enableSaveButton_WhenNameAndEmailAreNotEmpty() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(name = "kim", email = "ail.com"),
                    navigateUp = {},
                    onSuccessToSave = {}
                )
            }

            onNodeWithContentDescription("저장하기").assertIsEnabled()
        }
    }

    @Test
    fun shouldLimitNameLength_WhenTypeName() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(name = "", onUpdate = {
                        assertTrue(it.name.length <= MAX_USER_NAME_LEN)
                    }),
                    navigateUp = {},
                    onSuccessToSave = {},
                )
            }

            val stringBuilder = StringBuilder()
            for (i in 1..(MAX_USER_NAME_LEN - 2)) {
                stringBuilder.append("가")
            }

            for (i in 1..4) {
                stringBuilder.append("가")
                onNodeWithText("이름").performTextInput(stringBuilder.toString())
            }
        }
    }

    @Test
    fun shouldDisableSaveButton_WhenEmailIsNotValid() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(email = "abc@.com"),
                    navigateUp = {},
                    onSuccessToSave = {},
                )
            }

            onNodeWithContentDescription("저장하기").assertIsNotEnabled()
        }
    }
}