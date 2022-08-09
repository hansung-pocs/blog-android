package com.pocs.presentation

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.post.GetAllPostsUseCase
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.IsCurrentUserUnknownUseCase
import com.pocs.presentation.view.home.article.ArticleViewModel
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private val userRepository = FakeUserRepositoryImpl()
    private val postRepository = FakePostRepositoryImpl()

    private lateinit var viewModel: ArticleViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldVisibleFab_WhenCurrentUserIsNotUnknown() {
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.MEMBER)
        initViewModel()

        Assert.assertTrue(viewModel.uiState.value.visiblePostWriteFab)
    }

    @Test
    fun shouldDoNotVisibleFab_WhenCurrentUserIsUnknown() {
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.UNKNOWN)
        initViewModel()

        Assert.assertFalse(viewModel.uiState.value.visiblePostWriteFab)
    }

    private fun initViewModel() {
        viewModel = ArticleViewModel(
            getAllPostsUseCase = GetAllPostsUseCase(postRepository),
            isCurrentUserUnknownUseCase = IsCurrentUserUnknownUseCase(
                GetCurrentUserTypeUseCase(userRepository)
            )
        )
    }
}