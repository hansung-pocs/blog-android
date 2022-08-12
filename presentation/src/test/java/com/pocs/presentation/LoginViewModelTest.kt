package com.pocs.presentation

import com.pocs.domain.usecase.auth.GetCurrentUserStateFlowUseCase
import com.pocs.domain.usecase.auth.LoginUseCase
import com.pocs.presentation.view.login.LoginViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldIsLoggedInIsTrue_WhenUserAlreadyLoggedIn() {
        authRepository.currentUser.value = mockNormalUserDetail

        initViewModel()

        assertTrue(viewModel.uiState.value.isLoggedIn)
    }

    @Test
    fun shouldIsLoggedInIsFalse_WhenUserDidNotLogin() {
        authRepository.currentUser.value = null

        initViewModel()

        assertFalse(viewModel.uiState.value.isLoggedIn)
    }

    @Test
    fun shouldIsLoggedInIsTrue_WhenUserSuccessToLogin() {
        authRepository.currentUser.value = null
        authRepository.loginResult = Result.success(Unit)
        initViewModel()

        viewModel.login()

        assertTrue(viewModel.uiState.value.isLoggedIn)
    }

    @Test
    fun shouldIsLoggedInIsFalse_WhenUserFailedToLogin() {
        authRepository.currentUser.value = null
        authRepository.loginResult = Result.failure(exception = Exception())
        initViewModel()

        viewModel.login()

        assertFalse(viewModel.uiState.value.isLoggedIn)
    }

    @Test
    fun shouldExistErrorMessage_WhenUserFailedToLogin() {
        val errorMessage = "로그인 실패"
        authRepository.currentUser.value = null
        authRepository.loginResult = Result.failure(exception = Exception(errorMessage))
        initViewModel()

        viewModel.login()

        assertEquals(errorMessage, viewModel.uiState.value.errorMessage)
    }

    private fun initViewModel() {
        viewModel = LoginViewModel(
            getCurrentUserStateFlowUseCase = GetCurrentUserStateFlowUseCase(authRepository),
            loginUseCase = LoginUseCase(authRepository)
        )
    }
}