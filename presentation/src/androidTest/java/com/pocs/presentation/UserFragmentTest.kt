package com.pocs.presentation

import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.GetAllUsersUseCase
import com.pocs.domain.usecase.user.SearchUserUseCase
import com.pocs.presentation.view.user.UserFragment
import com.pocs.presentation.view.user.UserViewModel
import com.pocs.test_library.extension.launchFragmentInHiltContainer
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockAnonymousUser
import com.pocs.test_library.mock.mockNormalUser
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class UserFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val userRepository = FakeUserRepositoryImpl()

    @BindValue
    val authRepository = FakeAuthRepositoryImpl()

    @BindValue
    lateinit var viewModel: UserViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldShowNoPermissionSnackBar_WhenAnonymousClickUserCard() = runTest {
        val user = mockNormalUser
        val pagingData = PagingData.from(listOf(user))
        authRepository.currentUser.value = mockAnonymousUser
        userRepository.userPagingFlow = MutableStateFlow(pagingData)
        initViewModel()
        launchFragmentInHiltContainer<UserFragment>(themeResId = R.style.Theme_PocsBlog)

        onView(withText(user.defaultInfo!!.name)).perform(click())

        onView(withText(R.string.can_see_only_member)).check(matches(isDisplayed()))
    }

    private fun initViewModel() {
        viewModel = UserViewModel(
            getAllUsersUseCase = GetAllUsersUseCase(userRepository),
            searchUserUseCase = SearchUserUseCase(userRepository),
            getCurrentUserTypeUseCase = GetCurrentUserTypeUseCase(authRepository)
        )
    }
}