package com.pocs.presentation

import android.content.Context
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.post.CanDeletePostUseCase
import com.pocs.domain.usecase.post.CanEditPostUseCase
import com.pocs.domain.usecase.post.DeletePostUseCase
import com.pocs.domain.usecase.post.GetPostDetailUseCase
import com.pocs.presentation.view.post.detail.PostDetailActivity
import com.pocs.presentation.view.post.detail.PostDetailViewModel
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import com.pocs.test_library.mock.mockPostDetail1
import com.pocs.test_library.mock.mockPostDetail2
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PostDetailActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    @BindValue
    val postRepository = FakePostRepositoryImpl()

    @BindValue
    val userRepository = FakeUserRepositoryImpl()

    @BindValue
    val viewModel = PostDetailViewModel(
        GetPostDetailUseCase(postRepository),
        DeletePostUseCase(postRepository = postRepository, userRepository = userRepository),
        CanEditPostUseCase(userRepository),
        CanDeletePostUseCase(userRepository)
    )

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        context = getInstrumentation().targetContext
    }

    @Test
    fun shouldShowDeletedKeyword_WhenPostWasDeleted() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)

        val intent = PostDetailActivity.getIntent(context, postDetail.id, isDeleted = true)
        launchActivity<PostDetailActivity>(intent)

        onView(withText(R.string.deleted)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNotShowDeletedKeyword_WhenPostWasNotDeleted() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)

        val intent = PostDetailActivity.getIntent(context, postDetail.id, isDeleted = false)
        launchActivity<PostDetailActivity>(intent)

        onView(withText(R.string.deleted)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldNotShowMoreInfoOptionButton_WhenPostWasDeleted() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)
        userRepository.currentUser = mockNormalUserDetail.copy(
            id = postDetail.writer.id,
            type = UserType.ADMIN
        )

        val intent = PostDetailActivity.getIntent(context, postDetail.id, isDeleted = true)
        launchActivity<PostDetailActivity>(intent)

        onView(withContentDescription(R.string.more_info_button)).check { _, noViewFoundException ->
            assertNotNull(noViewFoundException)
        }
    }

    @Test
    fun shouldNotShowMoreInfoOptionButton_WhenPostWasNotDeleted_AndUserIsNotAdminAndIsNotWriter() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)
        userRepository.currentUser = mockNormalUserDetail.copy(
            id = 98765341,
            type = UserType.MEMBER
        )

        val intent = PostDetailActivity.getIntent(context, postDetail.id, isDeleted = false)
        launchActivity<PostDetailActivity>(intent)

        onView(withContentDescription(R.string.more_info_button)).check { _, noViewFoundException ->
            assertNotNull(noViewFoundException)
        }
    }

    @Test
    fun shouldShowDeleteOption_WhenPostWasNotDeleted_AndUserIsWriter() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)
        userRepository.currentUser = mockNormalUserDetail.copy(
            id = postDetail.writer.id,
            type = UserType.MEMBER
        )

        val intent = PostDetailActivity.getIntent(context, postDetail.id, isDeleted = false)
        launchActivity<PostDetailActivity>(intent).onActivity {
            it.openOptionsMenu()
        }

        onView(withText(R.string.delete)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNotShowEditOption_WhenPostWasNotDeleted_AndUserIsNotWriter() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)
        userRepository.currentUser = mockNormalUserDetail.copy(
            id = 96412433,
            type = UserType.ADMIN
        )

        val intent = PostDetailActivity.getIntent(context, postDetail.id, isDeleted = false)
        launchActivity<PostDetailActivity>(intent).onActivity {
            it.openOptionsMenu()
        }

        onView(withText(R.string.edit)).check { _, noViewFoundException ->
            assertNotNull(noViewFoundException)
        }
    }

    @Test
    fun shouldShowEditOption_WhenPostWasNotDeleted_AndUserIsWriter() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)
        userRepository.currentUser = mockNormalUserDetail.copy(
            id = postDetail.writer.id,
            type = UserType.MEMBER
        )

        val intent = PostDetailActivity.getIntent(context, postDetail.id, isDeleted = false)
        launchActivity<PostDetailActivity>(intent).onActivity {
            it.openOptionsMenu()
        }

        onView(withText(R.string.edit)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowSnackBar_WhenPostHasBeenEdited() {
        val postDetail = mockPostDetail2
        postRepository.postDetailResult = Result.success(postDetail)
        postRepository.updatePostResult = Result.success(Unit)
        userRepository.currentUser = mockNormalUserDetail.copy(
            id = postDetail.writer.id,
            type = UserType.MEMBER
        )

        val intent = PostDetailActivity.getIntent(context, postDetail.id, isDeleted = false)
        launchActivity<PostDetailActivity>(intent).onActivity {
            it.openOptionsMenu()
        }
        onView(withText(R.string.edit)).perform(click())

        composeRule.onNodeWithContentDescription(context.getString(R.string.save)).performClick()

        onView(withText(R.string.post_edited)).check(matches(isDisplayed()))
    }
}