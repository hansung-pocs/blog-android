package com.pocs.presentation

import com.pocs.domain.usecase.admin.KickUserUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.user.GetUserDetailUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.view.user.detail.UserDetailViewModel
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
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
class UserDetailViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private val authRepository = FakeAuthRepositoryImpl()
    private val userRepository = FakeUserRepositoryImpl()
    private val adminRepository = FakeAdminRepositoryImpl()

    private lateinit var viewModel: UserDetailViewModel

    private val mockUserDetail = mockNormalUserDetail

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = UserDetailViewModel(
            getUserDetailUseCase = GetUserDetailUseCase(
                userRepository = userRepository,
                adminRepository = adminRepository,
                getCurrentUserTypeUseCase = GetCurrentUserTypeUseCase(authRepository)
            ),
            IsCurrentUserAdminUseCase(GetCurrentUserTypeUseCase(authRepository)),
            GetCurrentUserUseCase(authRepository),
            KickUserUseCase(adminRepository)
        )
        authRepository.currentUser.value = mockUserDetail
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldUiStateIsSuccess_WhenSuccessToGetUserDetail() {
        userRepository.userDetailResult = Result.success(mockUserDetail)

        viewModel.fetchUserInfo(mockUserDetail.id)

        assertTrue(viewModel.uiState is UserDetailUiState.Success)
    }

    @Test
    fun shouldUiStateIsFailure_WhenFailToGetUserDetail() {
        userRepository.userDetailResult = Result.failure(Exception())

        viewModel.fetchUserInfo(mockUserDetail.id)

        assertTrue(viewModel.uiState is UserDetailUiState.Failure)
    }

    @Test
    fun shouldIsMyInfoIsTrue_WhenSameUserDetailIdAndCurrentUserId() {
        userRepository.userDetailResult = Result.success(mockUserDetail)

        viewModel.fetchUserInfo(mockUserDetail.id)

        val uiState = viewModel.uiState as UserDetailUiState.Success
        assertTrue(uiState.isMyInfo)
    }

    @Test
    fun shouldIsMyInfoIsFalse_WhenDifferentUserDetailIdAndCurrentUserId() {
        userRepository.userDetailResult = Result.success(mockUserDetail)

        viewModel.fetchUserInfo(mockUserDetail.id + 3)

        val uiState = viewModel.uiState as UserDetailUiState.Success
        assertFalse(uiState.isMyInfo)
    }
}