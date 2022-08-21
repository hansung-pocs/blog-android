package com.pocs.presentation

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pocs.presentation.model.auth.LoginUiState
import com.pocs.presentation.view.login.LoginContent
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun disableLoginButton_WhenUserNameIsEmtpy() {
        val uiState = LoginUiState(
            userName = "",
            password = "123",
            onUpdate = {}
        )
        composeRule.run {
            setContent {
                LoginContent(
                    uiState = uiState,
                    onLoginClick = {},
                    onBrowseAsAnonymousClick = {},
                    onErrorMessageShown = {}
                )
            }

            onNodeWithText("로그인").assertIsNotEnabled()
        }
    }

    @Test
    fun disableLoginButton_WhenPasswordIsEmtpy() {
        val uiState = LoginUiState(
            userName = "32",
            password = "",
            onUpdate = {}
        )
        composeRule.run {
            setContent {
                LoginContent(
                    uiState = uiState,
                    onLoginClick = {},
                    onBrowseAsAnonymousClick = {},
                    onErrorMessageShown = {}
                )
            }

            onNodeWithText("로그인").assertIsNotEnabled()
        }
    }

    @Test
    fun enableLoginButton_WhenUserNameAndPasswordIsNotEmtpy() {
        val uiState = LoginUiState(
            userName = "423",
            password = "123",
            onUpdate = {}
        )
        composeRule.run {
            setContent {
                LoginContent(
                    uiState = uiState,
                    onLoginClick = {},
                    onBrowseAsAnonymousClick = {},
                    onErrorMessageShown = {}
                )
            }

            onNodeWithText("로그인").assertIsEnabled()
        }
    }
}