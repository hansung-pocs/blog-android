package com.pocs.presentation

import android.app.Activity.RESULT_OK
import android.content.Context
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.presentation.view.post.edit.PostEditActivity
import com.pocs.presentation.view.post.edit.PostEditViewModel
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
    val viewModel = PostEditViewModel()

    lateinit var context: Context

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

        assertEquals(RESULT_OK, scenario.result.resultCode)
    }
}