package com.pocs.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.pocs.domain.usecase.admin.GetAllUsersAsAdminUseCase
import com.pocs.presentation.view.admin.user.AdminUserFragment
import com.pocs.presentation.view.admin.user.AdminUserViewModel
import com.pocs.test_library.extension.launchFragmentInHiltContainer
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
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
    val repository = FakeAdminRepositoryImpl()

    @BindValue
    lateinit var viewModel: AdminUserViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun initViewModel() {
        viewModel = AdminUserViewModel(GetAllUsersAsAdminUseCase(repository))
    }

    @Test
    fun showUserData_AfterInitViewModel() = runTest {
        repository.userList = listOf(mockNormalUser)
        initViewModel()

        launchFragmentInHiltContainer<AdminUserFragment>(themeResId = R.style.Theme_PocsBlog)

        onView(withText(mockNormalUser.name)).check(matches(isDisplayed()))
        onView(withSubstring(mockNormalUser.studentId.toString())).check(matches(isDisplayed()))
        onView(withSubstring(mockNormalUser.generation.toString())).check(matches(isDisplayed()))
    }

    @Test
    fun showKickedText_WhenUserWasKicked() = runTest {
        repository.userList = listOf(mockKickedUser)
        initViewModel()

        launchFragmentInHiltContainer<AdminUserFragment>(themeResId = R.style.Theme_PocsBlog)

        onView(withSubstring("탈퇴됨")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNotShowKickedText_WhenUserWasNotKicked() = runTest {
        repository.userList = listOf(mockNormalUser)
        initViewModel()

        launchFragmentInHiltContainer<AdminUserFragment>(themeResId = R.style.Theme_PocsBlog)

        onView(withSubstring("탈퇴됨")).check { _, noViewFoundException ->
            assertNotNull(noViewFoundException)
        }
    }
}