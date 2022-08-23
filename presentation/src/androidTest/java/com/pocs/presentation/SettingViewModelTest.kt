package com.pocs.presentation

import com.pocs.domain.usecase.auth.GetCurrentUserStateFlowUseCase
import com.pocs.domain.usecase.auth.LogoutUseCase
import com.pocs.presentation.view.setting.SettingViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private lateinit var viewModel: SettingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldOnSuccessToLogoutIsTrue_WhenLogoutSuccess() {
        authRepository.currentUser.value = mockAdminUserDetail
        authRepository.logoutResult = Result.success(Unit)
        initViewModel()

        viewModel.logout()

        assertTrue(viewModel.uiState.onSuccessToLogout)
    }

    @Test
    fun shouldExistErrorMessage_WhenLogoutFailure() {
        val errorMessage = "error"
        authRepository.currentUser.value = mockAdminUserDetail
        authRepository.logoutResult = Result.failure(Exception(errorMessage))
        initViewModel()

        viewModel.logout()

        assertEquals(errorMessage, viewModel.uiState.errorMessage)
    }

    private fun initViewModel() {
        viewModel = SettingViewModel(
            GetCurrentUserStateFlowUseCase(authRepository),
            LogoutUseCase(authRepository)
        )
    }
}