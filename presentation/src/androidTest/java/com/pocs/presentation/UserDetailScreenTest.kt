package com.pocs.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.pocs.presentation.mapper.toUiState
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.view.user.detail.UserDetailScreen
import com.pocs.test_library.mock.mockAdminUserDetail
import org.junit.Rule
import org.junit.Test
import java.net.ConnectException

class UserDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val userDetail = mockAdminUserDetail.toUiState()

    @Test
    fun showUserDetailContent_WhenUiStateIsSuccess() {
        val uiState = UserDetailUiState.Success(
            userDetail,
            isCurrentUserAdmin = false,
            shownErrorMessage = {},
            isMyInfo = true,
            successToKick = false,
            onKickClick = {}
        )
        composeTestRule.setContent {
            UserDetailScreen(
                uiState = uiState,
                onEdited = {},
                onSuccessToKick = {}
            )
        }

        composeTestRule.onNodeWithText(userDetail.defaultInfo!!.email).assertIsDisplayed()
    }

    @Test
    fun showFailureContent_WhenUiStateIsFailure() {
        val exception = Exception("에러")
        val uiState = UserDetailUiState.Failure(exception, onRetryClick = {})
        composeTestRule.setContent {
            UserDetailScreen(
                uiState = uiState,
                onEdited = {},
                onSuccessToKick = {}
            )
        }

        composeTestRule.onNodeWithText(userDetail.defaultInfo!!.email).assertDoesNotExist()
        composeTestRule.onNodeWithText(exception.message!!).assertIsDisplayed()
    }

    @Test
    fun showMoreInfoButton_WhenCurrentUserIsAdmin() {
        val uiState = UserDetailUiState.Success(
            userDetail,
            isCurrentUserAdmin = true,
            shownErrorMessage = {},
            isMyInfo = true,
            successToKick = false,
            onKickClick = {}
        )

        composeTestRule.run {
            setContent { UserDetailScreen(uiState = uiState, onEdited = {}, onSuccessToKick = {}) }

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
            successToKick = false,
            onKickClick = {}
        )

        composeTestRule.run {
            setContent { UserDetailScreen(uiState = uiState, onEdited = {}, onSuccessToKick = {}) }

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
            successToKick = false,
            onKickClick = {}
        )

        composeTestRule.run {
            setContent { UserDetailScreen(uiState = uiState, onEdited = {}, onSuccessToKick = {}) }

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
            successToKick = false,
            onKickClick = {}
        )

        composeTestRule.run {
            setContent { UserDetailScreen(uiState = uiState, onEdited = {}, onSuccessToKick = {}) }

            onNodeWithText(errorMessage).assertIsDisplayed()
        }
    }

    @Test
    fun shouldNotShowServerUrl_WhenExceptionIsConnectException() {
        val exception = ConnectException("Failed to connect http://123.123.123/")
        val uiState = UserDetailUiState.Failure(exception, onRetryClick = {})
        composeTestRule.setContent {
            UserDetailScreen(
                uiState = uiState,
                onEdited = {},
                onSuccessToKick = {}
            )
        }

        composeTestRule.onNodeWithText(exception.message!!).assertDoesNotExist()
    }
}
