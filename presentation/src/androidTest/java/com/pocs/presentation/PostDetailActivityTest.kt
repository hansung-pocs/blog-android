package com.pocs.presentation

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.launchActivity
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.comment.GetCommentsUseCase
import com.pocs.domain.usecase.post.CanDeletePostUseCase
import com.pocs.domain.usecase.post.CanEditPostUseCase
import com.pocs.domain.usecase.post.DeletePostUseCase
import com.pocs.domain.usecase.post.GetPostDetailUseCase
import com.pocs.presentation.view.post.detail.PostDetailActivity
import com.pocs.presentation.view.post.detail.PostDetailViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import com.pocs.test_library.mock.mockPostDetail1
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PostDetailActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    @BindValue
    val postRepository = FakePostRepositoryImpl()

    @BindValue
    val authRepository = FakeAuthRepositoryImpl()

    @BindValue
    val viewModel = PostDetailViewModel(
        GetPostDetailUseCase(postRepository),
        DeletePostUseCase(postRepository = postRepository, authRepository = authRepository),
        CanEditPostUseCase(authRepository),
        CanDeletePostUseCase(authRepository),
        GetCommentsUseCase(),
        GetCurrentUserUseCase(authRepository)
    )

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        context = getInstrumentation().targetContext
    }

    @Test
    fun shouldNotShowMoreInfoOptionButton_WhenUserIsNotAdminAndIsNotWriter() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)
        authRepository.currentUser.value = mockNormalUserDetail.copy(
            id = 98765341,
            type = UserType.MEMBER
        )

        val intent = PostDetailActivity.getIntent(context, postDetail.id)
        launchActivity<PostDetailActivity>(intent)

        findMoreInfoButton().assertDoesNotExist()
    }

    @Test
    fun shouldShowDeleteOption_WhenUserIsWriter() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)
        authRepository.currentUser.value = mockNormalUserDetail.copy(
            id = postDetail.writer.id,
            type = UserType.MEMBER
        )
        val intent = PostDetailActivity.getIntent(context, postDetail.id)
        launchActivity<PostDetailActivity>(intent)

        openDropdownMenu()

        findDeleteText().assertIsDisplayed()
    }

    @Test
    fun shouldNotShowEditOption_WhenUserIsNotWriter() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)
        authRepository.currentUser.value = mockNormalUserDetail.copy(
            id = 96412433,
            type = UserType.ADMIN
        )
        val intent = PostDetailActivity.getIntent(context, postDetail.id)
        launchActivity<PostDetailActivity>(intent)

        openDropdownMenu()

        findEditText().assertDoesNotExist()
    }

    @Test
    fun shouldShowEditOption_WhenUserIsWriter() {
        val postDetail = mockPostDetail1
        postRepository.postDetailResult = Result.success(postDetail)
        authRepository.currentUser.value = mockNormalUserDetail.copy(
            id = postDetail.writer.id,
            type = UserType.MEMBER
        )
        val intent = PostDetailActivity.getIntent(context, postDetail.id)
        launchActivity<PostDetailActivity>(intent)

        openDropdownMenu()

        findEditText().assertIsDisplayed()
    }

    private fun openDropdownMenu() {
        composeRule.onNodeWithContentDescription("더보기 버튼").performClick()
    }

    private fun findEditText(): SemanticsNodeInteraction {
        return composeRule.onNodeWithText("편집")
    }

    private fun findDeleteText(): SemanticsNodeInteraction {
        return composeRule.onNodeWithText("삭제")
    }

    private fun findMoreInfoButton(): SemanticsNodeInteraction {
        return composeRule.onNodeWithContentDescription("더보기 버튼")
    }
}