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
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.admin.KickUserUseCase
import com.pocs.domain.usecase.user.GetCurrentUserDetailUseCase
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.GetUserDetailUseCase
import com.pocs.domain.usecase.user.IsCurrentUserAdminUseCase
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.view.user.detail.UserDetailActivity
import com.pocs.presentation.view.user.detail.UserDetailViewModel
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
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
    val viewModel = UserDetailViewModel(
        GetUserDetailUseCase(
            userRepository = userRepository,
            adminRepository = adminRepository,
            GetCurrentUserTypeUseCase(userRepository)
        ),
        IsCurrentUserAdminUseCase(GetCurrentUserTypeUseCase(userRepository)),
        GetCurrentUserDetailUseCase(userRepository),
        KickUserUseCase(adminRepository)
    )

    private lateinit var context: Context

    private val userDetail = UserDetail(
        id = 1,
        name = "박민석",
        email = "hello@gmiad.com",
        studentId = 18294012,
        type = UserType.MEMBER,
        company = "google",
        generation = 10,
        github = "https://github.com/",
        createdAt = "",
        canceledAt = null
    )

    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().targetContext
        userRepository.currentUser = userDetail.copy(type = UserType.MEMBER)
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
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.ADMIN)
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
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.ADMIN)
        adminRepository.userDetailResult = Result.success(userDetail.copy(canceledAt = null))
        adminRepository.kickUserResult = Result.success(Unit)
        val intent = UserDetailActivity.getIntent(context, userDetail.id)
        launchActivity<UserDetailActivity>(intent)
        composeRule.onNodeWithText("탈퇴됨").assertDoesNotExist()

        adminRepository.userDetailResult = Result.success(userDetail.copy(
            canceledAt = "2022-08-09"
        ))
        composeRule.onNodeWithContentDescription("더보기 버튼").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()

        composeRule.onNodeWithText("탈퇴됨").assertIsDisplayed()
    }

    @Test
    fun shouldNotShowKickButton_WhenUserHasBeenKicked() {
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.ADMIN)
        adminRepository.userDetailResult = Result.success(userDetail.copy(canceledAt = null))
        adminRepository.kickUserResult = Result.success(Unit)
        val intent = UserDetailActivity.getIntent(context, userDetail.id)
        launchActivity<UserDetailActivity>(intent)

        adminRepository.userDetailResult = Result.success(userDetail.copy(
            canceledAt = "2022-08-09"
        ))
        composeRule.onNodeWithContentDescription("더보기 버튼").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()
        composeRule.onNodeWithText("강퇴하기").performClick()
        composeRule.onNodeWithContentDescription("더보기 버튼").performClick()

        composeRule.onNodeWithText("강퇴하기").assertDoesNotExist()
    }
}