package com.pocs.presentation

import android.content.Context
import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.admin.KickUserUseCase
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.GetUserDetailUseCase
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.view.user.detail.UserDetailActivity
import com.pocs.presentation.view.user.detail.UserDetailViewModel
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.HiltTestActivity
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
        GetCurrentUserTypeUseCase(userRepository),
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
        canceledAt = ""
    )

    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().targetContext
        userRepository.currentUser = userDetail.copy(type = UserType.MEMBER)
    }

    @Test
    fun shouldLoadUserDetail_WhenLifecycleReachedOnResume() {
        userRepository.userDetailResult = Result.success(userDetail)

        val intent = UserDetailActivity.getIntent(context, userDetail.id)
        val scenario = launchActivity<UserDetailActivity>(intent)

        assertEquals(userDetail.id, (viewModel.uiState as UserDetailUiState.Success).userDetail.id)

        val exception = Exception("error")
        userRepository.userDetailResult = Result.failure(exception)

        scenario.onActivity {
            it.startActivity(Intent(context, HiltTestActivity::class.java))
        }

        onView(isRoot()).perform(ViewActions.pressBack())

        assertEquals(exception, (viewModel.uiState as UserDetailUiState.Failure).e)
    }
}