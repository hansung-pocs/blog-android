package com.pocs.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.pocs.domain.usecase.admin.GetAllUsersAsAdminUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.presentation.view.admin.user.AdminUserFragment
import com.pocs.presentation.view.admin.user.AdminUserViewModel
import com.pocs.test_library.extension.launchFragmentInHiltContainer
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockKickedUser
import com.pocs.test_library.mock.mockNormalUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class AdminUserFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val adminRepository = FakeAdminRepositoryImpl()

    @BindValue
    val userRepository = FakeUserRepositoryImpl()

    @BindValue
    val authRepository = FakeAuthRepositoryImpl()

    @BindValue
    lateinit var viewModel: AdminUserViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun initViewModel() {
        viewModel = AdminUserViewModel(
            GetAllUsersAsAdminUseCase(adminRepository),
            GetCurrentUserTypeUseCase(authRepository)
        )
    }

    @Test
    fun showUserData_AfterInitViewModel() = runTest {
        adminRepository.userList = listOf(mockNormalUser)
        initViewModel()

        launchFragmentInHiltContainer<AdminUserFragment>(themeResId = R.style.Theme_PocsBlog)

        val defaultInfo = mockNormalUser.defaultInfo!!
        onView(withText(defaultInfo.name)).check(matches(isDisplayed()))
        onView(withSubstring(defaultInfo.studentId.toString())).check(matches(isDisplayed()))
        onView(withSubstring(defaultInfo.generation.toString())).check(matches(isDisplayed()))
    }

    @Test
    fun showKickedText_WhenUserWasKicked() = runTest {
        adminRepository.userList = listOf(mockKickedUser)
        initViewModel()

        launchFragmentInHiltContainer<AdminUserFragment>(themeResId = R.style.Theme_PocsBlog)

        onView(withSubstring("탈퇴됨")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNotShowKickedText_WhenUserWasNotKicked() = runTest {
        adminRepository.userList = listOf(mockNormalUser)
        initViewModel()

        launchFragmentInHiltContainer<AdminUserFragment>(themeResId = R.style.Theme_PocsBlog)

        onView(withSubstring("탈퇴됨")).check { _, noViewFoundException ->
            assertNotNull(noViewFoundException)
        }
    }
}