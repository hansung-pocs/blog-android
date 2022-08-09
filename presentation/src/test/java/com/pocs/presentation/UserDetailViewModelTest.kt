package com.pocs.presentation

import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.admin.KickUserUseCase
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.GetUserDetailUseCase
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.view.user.detail.UserDetailViewModel
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private val userRepository = FakeUserRepositoryImpl()
    private val adminRepository = FakeAdminRepositoryImpl()

    private lateinit var viewModel: UserDetailViewModel

    private val mockUserDetail = UserDetail(
        2,
        "김민성",
        "1871034",
        3,
        UserType.MEMBER,
        "google",
        30,
        "https://github/jja08111",
        "",
        null
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = UserDetailViewModel(
            getUserDetailUseCase = GetUserDetailUseCase(
                userRepository = userRepository,
                adminRepository = adminRepository,
                getCurrentUserTypeUseCase = GetCurrentUserTypeUseCase(userRepository)
            ),
            GetCurrentUserTypeUseCase(userRepository),
            KickUserUseCase(adminRepository)
        )
        userRepository.currentUser = mockUserDetail
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
}