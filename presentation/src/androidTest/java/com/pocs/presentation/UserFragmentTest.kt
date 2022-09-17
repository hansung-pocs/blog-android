package com.pocs.presentation

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.domain.model.user.UserDefaultInfo
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.GetAllUsersUseCase
import com.pocs.domain.usecase.user.SearchUserUseCase
import com.pocs.presentation.view.user.UserFragment
import com.pocs.presentation.view.user.UserViewModel
import com.pocs.test_library.extension.launchFragmentInHiltContainer
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockMemberUserDetail
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

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    @BindValue
    val userRepository = FakeUserRepositoryImpl()

    @BindValue
    val authRepository = FakeAuthRepositoryImpl()

    @BindValue
    lateinit var viewModel: UserViewModel

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(UnconfinedTestDispatcher())
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldNavigateUserDetailActivity_WhenClickUserCard() = runTest {
        val userData = mockNormalUser.copy(
            defaultInfo = UserDefaultInfo(
                name = "foobar",
                email = "",
                studentId = 1,
                generation = 1
            )
        )
        val userDetailData = mockMemberUserDetail.copy(defaultInfo = userData.defaultInfo)
        val pagingData = PagingData.from(listOf(userData))
        authRepository.currentUser.value = mockMemberUserDetail
        userRepository.userPagingFlow = MutableStateFlow(pagingData)
        userRepository.userDetailResult = Result.success(userDetailData)
        initViewModel()
        launchFragmentInHiltContainer<UserFragment>(themeResId = R.style.Theme_PocsBlog)

        onView(withText(userData.defaultInfo!!.name)).perform(click())

        val userDetailScreenTitle = context.getString(
            R.string.user_info_title,
            userData.defaultInfo!!.name
        )
        composeRule.onNodeWithText(userDetailScreenTitle).assertIsDisplayed()
    }

    private fun initViewModel() {
        viewModel = UserViewModel(
            getAllUsersUseCase = GetAllUsersUseCase(userRepository),
            searchUserUseCase = SearchUserUseCase(userRepository),
            getCurrentUserTypeUseCase = GetCurrentUserTypeUseCase(authRepository)
        )
    }
}
