package com.pocs.presentation

import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.presentation.constant.MAX_USER_NAME_LEN
import com.pocs.presentation.model.user.UserEditUiState
import com.pocs.presentation.view.user.edit.UserEditContent
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var mockUiState by mutableStateOf(UserEditUiState(
        id = 1,
        password = "",
        name = "박민석",
        email = "hello@gmiad.com",
        profileImageUrl = null,
        company = "google",
        github = "https://github.com/user",
        isInSaving = false,
        onUpdate = ::onUpdate,
        onSave = { Result.success(Unit) }
    ))

    private lateinit var context: Context

    private fun getString(@StringRes resId: Int) = context.getString(resId)

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    private fun onUpdate(userEditUiState: UserEditUiState) {
        mockUiState = userEditUiState
    }

    @Test
    fun showRecheckDialog_WhenUserClickBackButton() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState,
                    snackBarHostState = remember { SnackbarHostState() },
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
                    snackBarHostState = remember { SnackbarHostState() },
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
    fun disableSaveButton_WhenNameIsEmpty() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(name = "", email = "abc"),
                    snackBarHostState = remember { SnackbarHostState() },
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
                    snackBarHostState = remember { SnackbarHostState() },
                    navigateUp = {},
                    onSuccessToSave = {}
                )
            }

            onNodeWithContentDescription("저장하기").assertIsNotEnabled()
        }
    }

    @Test
    fun enableSaveButton_WhenAllInputsAreValid() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(
                        name = "kim",
                        email = "ail@he.com",
                        github = "https://github.com/user"
                    ),
                    snackBarHostState = remember { SnackbarHostState() },
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
                    snackBarHostState = remember { SnackbarHostState() },
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
                onNodeWithContentDescription("이름 입력창").performTextInput(stringBuilder.toString())
            }
        }
    }

    @Test
    fun shouldDisableSaveButton_WhenEmailIsNotValid() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(email = "abc@.com"),
                    snackBarHostState = remember { SnackbarHostState() },
                    navigateUp = {},
                    onSuccessToSave = {},
                )
            }

            onNodeWithContentDescription("저장하기").assertIsNotEnabled()
        }
    }

    @Test
    fun showNameMustBeNeededError_WhenNameIsEmpty() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(name = ""),
                    snackBarHostState = remember { SnackbarHostState() },
                    navigateUp = {},
                    onSuccessToSave = {},
                )
            }

            onNodeWithText("이름은 필수 사항입니다").assertIsDisplayed()
        }
    }

    @Test
    fun showEmailMustBeNeededError_WhenEmailIsEmpty() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(email = ""),
                    snackBarHostState = remember { SnackbarHostState() },
                    navigateUp = {},
                    onSuccessToSave = {},
                )
            }

            onNodeWithText("이메일은 필수 사항입니다").assertIsDisplayed()
        }
    }

    @Test
    fun shouldClearProfileImageUrl_WhenClickRemoveProfileImageButton() {
        composeTestRule.run {
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(profileImageUrl = "https://user-images.githubusercontent.com/57604817/179393181-8521a33b-a344-4f12-afb5-7cb3e77c44f2.png"),
                    snackBarHostState = remember { SnackbarHostState() },
                    navigateUp = {},
                    onSuccessToSave = {},
                )
            }

            onNodeWithContentDescription(getString(R.string.edit_user_image)).performClick()
            onNodeWithText(getString(R.string.remove_profile_image)).performClick()

            assertNull(mockUiState.profileImageUrl)
        }
    }

    @Test
    fun shouldClearNewProfileImage_WhenClickRemoveProfileImageButton() {
        composeTestRule.run {
            val bitmap = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.ic_baseline_add_24
            )
            setContent {
                UserEditContent(
                    uiState = mockUiState.copy(newProfileImage = bitmap),
                    snackBarHostState = remember { SnackbarHostState() },
                    navigateUp = {},
                    onSuccessToSave = {},
                )
            }

            onNodeWithContentDescription(getString(R.string.edit_user_image)).performClick()
            onNodeWithText(getString(R.string.remove_profile_image)).performClick()

            assertNull(mockUiState.profileImageUrl)
        }
    }
}