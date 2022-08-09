package com.pocs.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.model.user.item.UserDetailItemUiState
import com.pocs.presentation.view.user.detail.UserDetailScreen
import org.junit.Rule
import org.junit.Test

class UserDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val userDetail = UserDetailItemUiState(
        1,
        "김민성",
        "jja08111@gmail.com",
        1234528,
        UserType.MEMBER,
        "Hello",
        30,
        "https://github.com/jja08111",
        "2022-04-04",
        null
    )

    @Test
    fun showUserDetailContent_WhenUiStateIsSuccess() {
        val uiState = UserDetailUiState.Success(
            userDetail,
            isCurrentUserAdmin = false,
            shownErrorMessage = {},
            isMyInfo = true,
            onKickClick = {}
        )
        composeTestRule.setContent { UserDetailScreen(uiState = uiState, onEdited = {}) }

        composeTestRule.onNodeWithText(userDetail.email).assertIsDisplayed()
    }

    @Test
    fun showFailureContent_WhenUiStateIsFailure() {
        val exception = Exception("에러")
        val uiState = UserDetailUiState.Failure(exception, onRetryClick = {})
        composeTestRule.setContent { UserDetailScreen(uiState = uiState, onEdited = {}) }

        composeTestRule.onNodeWithText(userDetail.email).assertDoesNotExist()
        composeTestRule.onNodeWithText(exception.message!!).assertIsDisplayed()
    }

    @Test
    fun showMoreInfoButton_WhenCurrentUserIsAdmin() {
        val uiState = UserDetailUiState.Success(
            userDetail,
            isCurrentUserAdmin = true,
            shownErrorMessage = {},
            isMyInfo = true,
            onKickClick = {}
        )

        composeTestRule.run {
            setContent { UserDetailScreen(uiState = uiState, onEdited = {}) }

            onNodeWithContentDescription("더보기 버튼").assertIsDisplayed()
        }
    }

    @Test
    fun shouldNotShowMoreInfoButton_WhenCurrentUserIsNotAdmin() {
        val uiState = UserDetailUiState.Success(
            userDetail,
            isCurrentUserAdmin = false,
            shownErrorMessage = {},
            isMyInfo = true,
            onKickClick = {}
        )

        composeTestRule.run {
            setContent { UserDetailScreen(uiState = uiState, onEdited = {}) }

            onNodeWithContentDescription("더보기 버튼").assertDoesNotExist()
        }
    }

    @Test
    fun showRecheckDialog_WhenClickKickUserButton() {
        val uiState = UserDetailUiState.Success(
            userDetail.copy(canceledAt = null),
            isCurrentUserAdmin = true,
            shownErrorMessage = {},
            isMyInfo = true,
            onKickClick = {}
        )

        composeTestRule.run {
            setContent { UserDetailScreen(uiState = uiState, onEdited = {}) }

            onNodeWithContentDescription("더보기 버튼").performClick()
            onNodeWithText("강퇴하기").performClick()

            onNodeWithText("정말로 강퇴하시겠습니까?").assertIsDisplayed()
        }
    }

    @Test
    fun showErrorSnackBar_WhenErrorMessageIsNotNull() {
        val errorMessage = "ERROR"
        val uiState = UserDetailUiState.Success(
            userDetail,
            isCurrentUserAdmin = true,
            errorMessage = errorMessage,
            shownErrorMessage = {},
            isMyInfo = true,
            onKickClick = {}
        )

        composeTestRule.run {
            setContent { UserDetailScreen(uiState = uiState, onEdited = {}) }

            onNodeWithText(errorMessage).assertIsDisplayed()
        }
    }
}