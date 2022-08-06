package com.pocs.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.pocs.presentation.model.admin.AdminUserCreateUiState
import com.pocs.presentation.view.admin.user.create.AdminUserCreateScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AdminUserCreateScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var mockUiState by mutableStateOf(AdminUserCreateUiState(
        onSave = {},
        shownErrorMessage = {},
        onUpdateCreateInfo = {}
    ))

    @Test
    fun showSnackBar_WhenErrorMessageIsNotNull() {
        composeTestRule.run {
            setContent {
                AdminUserCreateScreen(
                    uiState = mockUiState,
                    navigateUp = {},
                    onSuccessToCreate = {}
                )
            }

            val errorMessage = "ERROR!!"
            onNodeWithText(errorMessage).assertDoesNotExist()

            mockUiState = mockUiState.copy(errorMessage = errorMessage)
            onNodeWithText(errorMessage).assertIsDisplayed()
        }
    }

    @Test
    fun shouldCallNavigateUp_WhenIsSuccessToSaveIsTrue() {
        composeTestRule.run {
            var callCount = 0
            setContent {
                AdminUserCreateScreen(
                    uiState = mockUiState,
                    navigateUp = { callCount++ },
                    onSuccessToCreate = {}
                )
            }

            assertEquals(0, callCount)

            mockUiState = mockUiState.copy(isSuccessToSave = true)
            mainClock.advanceTimeByFrame()

            assertEquals(1, callCount)
        }
    }

    @Test
    fun shouldDisableSaveButton_WhenEmailIsNotValid() {
        composeTestRule.run {
            var callCount = 0
            setContent {
                AdminUserCreateScreen(
                    uiState = mockUiState.copy(
                        createInfo = mockUiState.createInfo.copy(email = "abd@fe")
                    ),
                    navigateUp = { callCount++ },
                    onSuccessToCreate = {}
                )
            }

            onNodeWithContentDescription("저장하기").assertIsNotEnabled()
        }
    }
}