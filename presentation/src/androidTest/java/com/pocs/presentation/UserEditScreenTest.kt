package com.pocs.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.pocs.presentation.model.user.UserEditUiState
import com.pocs.presentation.view.user.edit.UserEditContent
import org.junit.Rule
import org.junit.Test

class UserEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var mockUiState by mutableStateOf(UserEditUiState(
        "박민석",
        "hello@gmiad.com",
        "18294012",
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
            setContent { UserEditContent(uiState = mockUiState) {} }

            onNodeWithContentDescription("뒤로가기").performClick()

            onNodeWithText("정말로 중단할까요?").assertIsDisplayed()
        }
    }

    @Test
    fun removeTextFieldValues_WhenClickClearButton() {
        composeTestRule.run {
            setContent { UserEditContent(uiState = mockUiState) {} }

            val name = mockUiState.name
            onNodeWithText(name).assertIsDisplayed()

            onAllNodesWithContentDescription("입력 창 내용 지우기")[0].performClick()

            onNodeWithText(name).assertDoesNotExist()
        }
    }
}