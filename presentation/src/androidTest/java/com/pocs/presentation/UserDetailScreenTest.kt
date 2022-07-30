package com.pocs.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pocs.domain.model.user.User
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.model.UserDetailUiState
import com.pocs.presentation.view.user.detail.UserDetailScreen
import org.junit.Rule
import org.junit.Test

class UserDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val user = User(
        1,
        "김민성",
        "jja08111@gmail.com",
        1234528,
        UserType.MEMBER,
        "Hello",
        30,
        "https://github.com/jja08111",
        "2022-04-04"
    )

    @Test
    fun showUserDetailContent_WhenUiStateIsSuccess() {
        val uiState = UserDetailUiState.Success(user)
        composeTestRule.setContent { UserDetailScreen(uiState = uiState) }

        composeTestRule.onNodeWithText(user.email).assertIsDisplayed()
    }

    @Test
    fun showFailureContent_WhenUiStateIsFailure() {
        val exception = Exception("에러")
        val uiState = UserDetailUiState.Failure(exception) {}
        composeTestRule.setContent { UserDetailScreen(uiState = uiState) }

        composeTestRule.onNodeWithText(user.email).assertDoesNotExist()
        composeTestRule.onNodeWithText(exception.message!!).assertIsDisplayed()
    }
}