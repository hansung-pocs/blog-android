package com.pocs.presentation

import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.auth.IsAuthReadyUseCase
import com.pocs.domain.usecase.auth.LoginUseCase
import com.pocs.presentation.view.login.LoginViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
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
    fun shouldIsLoggedInIsTrue_WhenUserAlreadyLoggedIn() = runTest {
        authRepository.currentUser.value = mockAdminUserDetail
        initViewModel()

        authRepository.emit(isReady = true)

        assertTrue(viewModel.uiState.value.isLoggedIn)
    }

    @Test
    fun shouldIsLoggedInIsFalse_WhenUserDidNotLogin() = runTest {
        authRepository.currentUser.value = null
        initViewModel()

        authRepository.emit(isReady = true)

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

    @Test
    fun shouldHideSplashScreenIsTrue_WhenEmit() = runTest {
        initViewModel()

        authRepository.emit(isReady = true)

        assertTrue(viewModel.uiState.value.hideSplashScreen)
    }

    // https://github.com/hansung-pocs/blog-android/issues/150 를 위한 테스트
    @Test
    fun shouldHideSplashScreenIsFalse_WhenCurrentUserExistsAndThenAuthIsReady() = runTest {
        initViewModel()

        authRepository.currentUser.value = mockAdminUserDetail
        authRepository.emit(isReady = true)

        assertFalse(viewModel.uiState.value.hideSplashScreen)
    }

    private fun initViewModel() {
        viewModel = LoginViewModel(
            isAuthReadyUseCase = IsAuthReadyUseCase(authRepository),
            getCurrentUserUseCase = GetCurrentUserUseCase(authRepository),
            loginUseCase = LoginUseCase(authRepository)
        )
    }
}