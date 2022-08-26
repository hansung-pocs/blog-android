package com.pocs.presentation

import androidx.paging.PagingData
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostFilterType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAnonymousUseCase
import com.pocs.domain.usecase.post.GetAllPostsUseCase
import com.pocs.presentation.view.home.HomeActivity
import com.pocs.presentation.view.home.HomeViewModel
import com.pocs.presentation.view.home.post.PostViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.mock.mockAnonymousUser
import com.pocs.test_library.mock.mockMemberUserDetail
import com.pocs.test_library.mock.mockPost
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
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class HomeActivityTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val postRepository = FakePostRepositoryImpl()

    @BindValue
    val authRepository = FakeAuthRepositoryImpl()

    @BindValue
    lateinit var homeViewModel: HomeViewModel

    @BindValue
    lateinit var postViewModel: PostViewModel

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
    fun shouldShowNoPermissionSnackBar_WhenAnonymousClickNonQnaPost() = runTest {
        val post = mockPost
        val pagingData = PagingData.from(listOf(post.copy(category = PostCategory.NOTICE)))
        authRepository.currentUser.value = mockAnonymousUser
        postRepository.postPagingDataFlow = MutableStateFlow(pagingData)
        initViewModels()
        launchActivity<HomeActivity>()

        onView(withText(post.title)).perform(click())

        onView(withText(R.string.can_see_only_member)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowNoPermissionSnackBar_WhenAnonymousClickPost_WhenSelectedChipIsNotice() = runTest {
        val post = mockPost
        val pagingData = PagingData.from(listOf(post.copy(category = PostCategory.NOTICE)))
        authRepository.currentUser.value = mockAnonymousUser
        postRepository.postPagingDataFlow = MutableStateFlow(pagingData)
        initViewModels()
        postViewModel.updatePostFilterType(PostFilterType.NOTICE)
        launchActivity<HomeActivity>()

        onView(withText(post.title)).perform(click())

        onView(withText(R.string.can_see_only_member)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisableUserListButtonAtDrawer_WhenCurrentUserIsAnonymous() {
        authRepository.currentUser.value = mockAnonymousUser
        initViewModels()
        launchActivity<HomeActivity>()

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())

        onView(withId(R.id.action_user_list)).check(matches(not(isEnabled())))
        onView(withText(R.string.user_list_only_member)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldEnableUserListButtonAtDrawer_WhenCurrentUserIsMember() {
        authRepository.currentUser.value = mockMemberUserDetail
        initViewModels()
        launchActivity<HomeActivity>()

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())

        onView(withId(R.id.action_user_list)).check(matches(isEnabled()))
    }

    private fun initViewModels() {
        homeViewModel = HomeViewModel(GetCurrentUserTypeUseCase(authRepository))
        postViewModel = PostViewModel(
            getAllPostsUseCase = GetAllPostsUseCase(postRepository),
            isCurrentUserAnonymousUseCase = IsCurrentUserAnonymousUseCase(
                getCurrentUserTypeUseCase = GetCurrentUserTypeUseCase(authRepository)
            )
        )
    }
}