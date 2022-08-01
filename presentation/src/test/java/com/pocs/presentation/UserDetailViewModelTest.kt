package com.pocs.presentation

import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.user.GetUserDetailUseCase
import com.pocs.presentation.model.user.UserDetailUiState
import com.pocs.presentation.view.user.detail.UserDetailViewModel
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

    private val repository = FakeUserRepositoryImpl()
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
        ""
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = UserDetailViewModel(GetUserDetailUseCase(repository))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldUiStateIsSuccess_WhenSuccessToGetUserDetail() {
        repository.userDetailResult = Result.success(mockUserDetail)

        viewModel.loadUserInfo(mockUserDetail.id)

        assertTrue(viewModel.uiState is UserDetailUiState.Success)
    }

    @Test
    fun shouldUiStateIsFailure_WhenFailToGetUserDetail() {
        repository.userDetailResult = Result.failure(Exception())

        viewModel.loadUserInfo(mockUserDetail.id)

        assertTrue(viewModel.uiState is UserDetailUiState.Failure)
    }
}