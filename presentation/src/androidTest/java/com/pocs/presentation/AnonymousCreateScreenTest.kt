package com.pocs.presentation

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pocs.presentation.constant.MIN_USER_PASSWORD_LEN
import com.pocs.presentation.model.user.anonymous.AnonymousCreateInfoUiState
import com.pocs.presentation.model.user.anonymous.AnonymousCreateUiState
import com.pocs.presentation.view.user.anonymous.AnonymousCreateScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AnonymousCreateScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldDisableCreateButton_WhenPasswordIsShort() {
        composeRule.run {
            val createInfo = AnonymousCreateInfoUiState(
                userName = "id",
                password = "hi"
            )
            assertTrue(createInfo.password.length < MIN_USER_PASSWORD_LEN)
            setContent {
                AnonymousCreateScreen(
                    uiState = AnonymousCreateUiState(
                        createInfo = createInfo,
                        onCreate = {},
                        onUpdateCreateInfo = {},
                        shownErrorMessage = {}
                    ),
                    navigateUp = {},
                    onSuccessToCreate = {}
                )
            }

            onNodeWithText("생성하기").assertIsNotEnabled()
        }
    }

    @Test
    fun shouldEnableCreateButton_WhenInfoIsCorrect() {
        composeRule.run {
            val createInfo = AnonymousCreateInfoUiState(
                userName = "id",
                password = "hiNiceToMeet"
            )
            assertTrue(createInfo.password.length >= MIN_USER_PASSWORD_LEN)
            setContent {
                AnonymousCreateScreen(
                    uiState = AnonymousCreateUiState(
                        createInfo = createInfo,
                        onCreate = {},
                        onUpdateCreateInfo = {},
                        shownErrorMessage = {}
                    ),
                    navigateUp = {},
                    onSuccessToCreate = {}
                )
            }

            onNodeWithText("생성하기").assertIsEnabled()
        }
    }
}
