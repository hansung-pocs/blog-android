package com.pocs.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.constant.MAX_USER_PASSWORD_LEN
import com.pocs.presentation.constant.MIN_USER_PASSWORD_LEN
import com.pocs.presentation.model.admin.AdminUserCreateUiState
import com.pocs.presentation.model.admin.UserCreateInfoUiState
import com.pocs.presentation.view.admin.user.create.AdminUserCreateScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AdminUserCreateScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val validUserCreateInfo = UserCreateInfoUiState(
        userName = "Name",
        password = "password",
        name = "Kim parkhong",
        studentId = "1820391",
        email = "abc@gmail.com",
        generation = "31",
        type = UserType.MEMBER
    )

    private var mockUiState by mutableStateOf(
        AdminUserCreateUiState(
            onSave = {},
            shownErrorMessage = {},
            onUpdateCreateInfo = {}
        )
    )

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
            setContent {
                AdminUserCreateScreen(
                    uiState = mockUiState.copy(
                        createInfo = validUserCreateInfo.copy(email = "abd@fe")
                    ),
                    navigateUp = {},
                    onSuccessToCreate = {}
                )
            }

            onNodeWithContentDescription("저장하기").assertIsNotEnabled()
        }
    }

    @Test
    fun shouldDisableSaveButton_WhenPasswordLengthIsShort() {
        composeTestRule.run {
            val createInfo = validUserCreateInfo.copy(password = "abd")
            assertTrue(createInfo.password.length < MAX_USER_PASSWORD_LEN)
            setContent {
                AdminUserCreateScreen(
                    uiState = mockUiState.copy(createInfo = createInfo),
                    navigateUp = {},
                    onSuccessToCreate = {}
                )
            }

            onNodeWithContentDescription("저장하기").assertIsNotEnabled()
        }
    }

    @Test
    fun shouldEnableSaveButton_WhenPasswordLengthIsUpperSix() {
        composeTestRule.run {
            val createInfo = validUserCreateInfo.copy(password = "helloNice")
            assertTrue(createInfo.password.length >= MIN_USER_PASSWORD_LEN)
            setContent {
                AdminUserCreateScreen(
                    uiState = mockUiState.copy(createInfo = createInfo),
                    navigateUp = {},
                    onSuccessToCreate = {}
                )
            }

            onNodeWithContentDescription("저장하기").assertIsEnabled()
        }
    }
}
