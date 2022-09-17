package com.pocs.presentation

import android.content.Context
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.domain.usecase.admin.GetAllPostsByUserUseCase
import com.pocs.presentation.view.post.by.user.PostByUserActivity
import com.pocs.presentation.view.post.by.user.PostByUserViewModel
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PostByUserActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var context: Context

    @BindValue
    val repository = FakeAdminRepositoryImpl()

    @BindValue
    val viewModel = PostByUserViewModel(
        GetAllPostsByUserUseCase(repository)
    )

    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun shouldShowUserName() {
        val userDetail = mockAdminUserDetail
        val name = userDetail.defaultInfo?.name ?: context.getString(
            R.string.anonymous_name,
            userDetail.id
        )
        val intent = PostByUserActivity.getIntent(
            context,
            userId = userDetail.id,
            name = name
        )
        launchActivity<PostByUserActivity>(intent)

        onView(withText(containsString(name))).check { _, noViewFoundException ->
            assertNull(noViewFoundException)
        }
    }

    @Test
    fun shouldShowEmptyText_WhenThereIsNoPostByUser() {
        repository.postListByUser = emptyList()
        val userDetail = mockAdminUserDetail
        val name = userDetail.defaultInfo?.name ?: context.getString(
            R.string.anonymous_name,
            userDetail.id
        )
        val intent = PostByUserActivity.getIntent(
            context,
            userId = userDetail.id,
            name = name
        )
        launchActivity<PostByUserActivity>(intent)

        onView(withText(R.string.empty)).check(matches(isDisplayed()))
    }
}
