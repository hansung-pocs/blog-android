package com.pocs.presentation

import android.app.Activity.RESULT_OK
import android.content.Context
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.data.di.RepositoryModule
import com.pocs.domain.repository.PostRepository
import com.pocs.domain.usecase.post.AddPostUseCase
import com.pocs.presentation.view.post.create.PostCreateActivity
import com.pocs.presentation.view.post.create.PostCreateViewModel
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.mock.mockPostDetail2
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PostCreateActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createEmptyComposeRule()

    @BindValue
    val repository: PostRepository = FakePostRepositoryImpl()

    @BindValue
    val viewModel = PostCreateViewModel(AddPostUseCase(repository))

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun shouldReturnResultOk_AfterSuccessAddingNewPost() {
        val post = mockPostDetail2
        val intent = PostCreateActivity.getIntent(context, post.category)
        val scenario = launchActivity<PostCreateActivity>(intent)

        composeRule.onNodeWithContentDescription("제목 입력창").performTextInput("제목")
        composeRule.onNodeWithContentDescription("내용 입력창").performTextInput("내용")

        composeRule.onNodeWithContentDescription("저장하기").performClick()

        assertEquals(RESULT_OK, scenario.result.resultCode)
    }
}