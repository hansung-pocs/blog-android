package com.pocs.presentation

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.post.GetAllPostsUseCase
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.IsCurrentUserAdminUseCase
import com.pocs.presentation.view.home.notice.NoticeViewModel
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoticeViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private val userRepository = FakeUserRepositoryImpl()
    private val postRepository = FakePostRepositoryImpl()

    private lateinit var viewModel: NoticeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldIsCurrentUserAdminIsTrue() {
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.ADMIN)
        initViewModel()

        assertTrue(viewModel.uiState.value.isCurrentUserAdmin)
    }

    @Test
    fun shouldIsCurrentUserAdminIsFalse() {
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.UNKNOWN)
        initViewModel()

        assertFalse(viewModel.uiState.value.isCurrentUserAdmin)
    }

    private fun initViewModel() {
        viewModel = NoticeViewModel(
            getAllPostsUseCase = GetAllPostsUseCase(postRepository),
            isCurrentUserAdminUseCase = IsCurrentUserAdminUseCase(
                GetCurrentUserTypeUseCase(userRepository)
            )
        )
    }
}