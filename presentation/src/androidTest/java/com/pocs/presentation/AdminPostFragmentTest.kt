package com.pocs.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.pocs.domain.usecase.admin.GetAllPostsAsAdminUseCase
import com.pocs.presentation.view.admin.post.AdminPostFragment
import com.pocs.presentation.view.admin.post.AdminPostViewModel
import com.pocs.test_library.extension.launchFragmentInHiltContainer
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.mock.mockPost
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AdminPostFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val repository = FakeAdminRepositoryImpl()

    @BindValue
    lateinit var viewModel: AdminPostViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun showsDeletedKeyword_IfPostWasDeleted() {
        repository.postList = listOf(mockPost.copy(canceledAt = "2022-01-02"))
        initViewModel()

        launchFragmentInHiltContainer<AdminPostFragment>(themeResId = R.style.Theme_PocsBlog)

        onView(withSubstring("삭제됨")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldNotShowDeletedKeyword_IfPostWasNotDeleted() {
        repository.postList = listOf(mockPost.copy(canceledAt = null))
        initViewModel()

        launchFragmentInHiltContainer<AdminPostFragment>(themeResId = R.style.Theme_PocsBlog)

        onView(withSubstring("삭제됨")).check { _, noViewFoundException ->
            assertNotNull(noViewFoundException)
        }
    }

    private fun initViewModel() {
        viewModel = AdminPostViewModel(GetAllPostsAsAdminUseCase(repository))
    }
}
