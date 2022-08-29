package com.pocs.presentation

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.admin.KickUserUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.user.GetUserDetailUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.presentation.extension.RESULT_REFRESH
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.view.user.detail.UserDetailActivity
import com.pocs.presentation.view.user.detail.UserDetailViewModel
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import com.pocs.test_library.mock.mockMemberUserDetail
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UserDetailActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    @BindValue
    val userRepository = FakeUserRepositoryImpl()

    @BindValue
    val adminRepository = FakeAdminRepositoryImpl()

    @BindValue
    val authRepository = FakeAuthRepositoryImpl()

    @BindValue
    val viewModel = UserDetailViewModel(
        GetUserDetailUseCase(
            userRepository = userRepository,
            adminRepository = adminRepository,
            GetCurrentUserTypeUseCase(authRepository)
        ),
        IsCurrentUserAdminUseCase(GetCurrentUserTypeUseCase(authRepository)),
        GetCurrentUserUseCase(authRepository),
        KickUserUseCase(adminRepository)
    )

    private lateinit var context: Context

    private val userDetail = mockMemberUserDetail

    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().targetContext
        authRepository.currentUser.value = userDetail.copy(type = UserType.MEMBER)
    }

    @Test
    fun shouldLoadUserDetail_WhenLaunchActivity() {
        userRepository.userDetailResult = Result.success(userDetail)

        val intent = UserDetailActivity.getIntent(context, userDetail.id)
        launchActivity<UserDetailActivity>(intent)

        assertEquals(userDetail.id, (viewModel.uiState as UserDetailUiState.Success).userDetail.id)
    }

    @Test
    fun shouldShowErrorSnackBar_WhenFailedToKickUser() {
        val errorMessage = "ERROR!!@!"
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ADMIN)
        adminRepository.userDetailResult = Result.success(userDetail)
        adminRepository.kickUserResult = Result.failure(Exception(errorMessage))
        val intent = UserDetailActivity.getIntent(context, userDetail.id)
        launchActivity<UserDetailActivity>(intent)

        composeRule.onNodeWithContentDescription("더보기 버튼").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()

        composeRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun shouldFetchUserDetail_WhenSuccessToKickUser() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ADMIN)
        adminRepository.userDetailResult = Result.success(userDetail.copy(canceledAt = null))
        adminRepository.kickUserResult = Result.success(Unit)
        val intent = UserDetailActivity.getIntent(context, userDetail.id)
        launchActivity<UserDetailActivity>(intent)
        composeRule.onNodeWithText("탈퇴됨").assertDoesNotExist()

        adminRepository.userDetailResult = Result.success(
            userDetail.copy(
                canceledAt = "2022-08-09"
            )
        )
        composeRule.onNodeWithContentDescription("더보기 버튼").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()

        composeRule.onNodeWithText("탈퇴됨").assertIsDisplayed()
    }

    @Test
    fun shouldNotShowKickButton_WhenUserHasBeenKicked() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ADMIN)
        adminRepository.userDetailResult = Result.success(userDetail.copy(canceledAt = null))
        adminRepository.kickUserResult = Result.success(Unit)
        val intent = UserDetailActivity.getIntent(context, userDetail.id)
        launchActivity<UserDetailActivity>(intent)

        adminRepository.userDetailResult = Result.success(
            userDetail.copy(
                canceledAt = "2022-08-09"
            )
        )
        composeRule.onNodeWithContentDescription("더보기 버튼").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()
        composeRule.onNodeWithContentDescription("더보기 버튼").performClick()

        composeRule.onNodeWithText("강퇴하기").assertDoesNotExist()
    }

    @Test
    fun shouldSetRefreshResult_WhenKickedUser() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ADMIN)
        adminRepository.userDetailResult = Result.success(userDetail.copy(canceledAt = null))
        adminRepository.kickUserResult = Result.success(Unit)
        val intent = UserDetailActivity.getIntent(context, userDetail.id)
        val scenario = launchActivity<UserDetailActivity>(intent)

        adminRepository.userDetailResult = Result.success(
            userDetail.copy(canceledAt = "2022-08-09")
        )
        with (composeRule) {
            onNodeWithContentDescription("더보기 버튼").performClick()
            onNodeWithText("강퇴하기").performClick()
            onNodeWithText("강퇴하기").performClick()
            onNodeWithText("탈퇴됨").assertIsDisplayed()
        }
        scenario.onActivity { it.finish() }

        assertEquals(RESULT_REFRESH, scenario.result.resultCode)
    }
}