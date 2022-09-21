package com.pocs.presentation

import android.view.View
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.tabs.TabLayout
import com.pocs.domain.usecase.admin.GetAllUsersAsAdminUseCase
import com.pocs.presentation.view.admin.AdminActivity
import com.pocs.presentation.view.admin.user.AdminUserViewModel
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockAnonymousUser
import com.pocs.test_library.mock.mockKickedUser
import com.pocs.test_library.mock.mockNormalUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
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
            GetAllUsersAsAdminUseCase(adminRepository)
        )
    }

    @Test
    fun showUserData_AfterInitViewModel() = runTest {
        adminRepository.userList = listOf(mockNormalUser)
        initViewModel()

        launchAdminUserFragment()

        val defaultInfo = mockNormalUser.defaultInfo!!
        onView(withText(defaultInfo.name)).check(matches(isDisplayed()))
        onView(withSubstring(defaultInfo.studentId.toString())).check(matches(isDisplayed()))
        onView(withSubstring(defaultInfo.generation.toString())).check(matches(isDisplayed()))
    }

    @Test
    fun showKickedText_WhenUserWasKicked() = runTest {
        adminRepository.userList = listOf(mockKickedUser)
        initViewModel()

        launchAdminUserFragment()

        onView(withSubstring("탈퇴됨")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNotShowKickedText_WhenUserWasNotKicked() = runTest {
        adminRepository.userList = listOf(mockNormalUser)
        initViewModel()

        launchAdminUserFragment()

        onView(withSubstring("탈퇴됨")).check { _, noViewFoundException ->
            assertNotNull(noViewFoundException)
        }
    }

    @Test
    fun shouldNotShowAnonymousUserSubtitle_WhenHiddenItemBySwipingUp() = runTest {
        val userList = mutableListOf(mockNormalUser)
        val normalUserStudentId = mockNormalUser.defaultInfo!!.studentId.toString()
        userList.addAll(List(100) { mockAnonymousUser })
        adminRepository.userList = userList
        initViewModel()
        launchAdminUserFragment()
        onView(withSubstring(normalUserStudentId)).check(matches(isDisplayed()))

        val matcher = allOf(
            withId(R.id.recyclerView),
            isDescendantOfA(withId(R.id.adminUserFragment))
        )
        onView(matcher).perform(swipeUp())

        onView(withSubstring(normalUserStudentId)).check(doesNotExist())
    }

    private fun launchAdminUserFragment() {
        launchActivity<AdminActivity>()
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(1))
    }

    @Suppress("SameParameterValue")
    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints(): Matcher<View>? {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))
            }

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }
}
