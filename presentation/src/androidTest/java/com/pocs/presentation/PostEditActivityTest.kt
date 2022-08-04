package com.pocs.presentation

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.domain.usecase.post.UpdatePostUseCase
import com.pocs.presentation.extension.RESULT_REFRESH
import com.pocs.presentation.view.post.edit.PostEditActivity
import com.pocs.presentation.view.post.edit.PostEditViewModel
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockPostDetail2
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
class PostEditActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    @BindValue
    val userRepository = FakeUserRepositoryImpl()

    @BindValue
    val postRepository = FakePostRepositoryImpl()

    @BindValue
    val viewModel = PostEditViewModel(UpdatePostUseCase(postRepository, userRepository))

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun shouldReturnResultOk_AfterSuccessSavingEditedPost() {
        val post = mockPostDetail2
        val intent = PostEditActivity.getIntent(
            context, post.id, post.title, post.content, post.category
        )
        val scenario = launchActivity<PostEditActivity>(intent)

        composeRule.onNodeWithContentDescription("저장하기").performClick()

        assertEquals(RESULT_REFRESH, scenario.result.resultCode)
    }

    @Test
    fun shouldShowErrorMessage_AfterFailedToEditedPost() {
        val post = mockPostDetail2
        val errorMessage = "ERROR"
        postRepository.updatePostResult = Result.failure(Exception(errorMessage))
        val intent = PostEditActivity.getIntent(
            context, post.id, post.title, post.content, post.category
        )
        launchActivity<PostEditActivity>(intent)

        composeRule.onNodeWithContentDescription("저장하기").performClick()

        composeRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}