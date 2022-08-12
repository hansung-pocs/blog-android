package com.pocs.presentation

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.auth.IsAuthReadyUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.presentation.view.home.HomeViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldIsCurrentUserAdminIsTrue_WhenCurrentUserIsAdmin() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.ADMIN)
        initViewModel()

        assertTrue(viewModel.uiState.value.isCurrentUserAdmin)
    }

    @Test
    fun shouldIsCurrentUserAdminIsFalse_WhenCurrentUserIsNotAdmin() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.MEMBER)
        initViewModel()

        assertFalse(viewModel.uiState.value.isCurrentUserAdmin)
    }

    // https://github.com/hansung-pocs/blog-android/issues/150 를 위한 테스트
    @Test
    fun shouldHideSplashScreenIsFalse_WhenCurrentUserIsNull() = runTest {
        initViewModel()

        authRepository.currentUser.value = null
        authRepository.emit(isReady = true)

        assertFalse(viewModel.uiState.value.hideSplashScreen)
    }

    @Test
    fun shouldHideSplashScreenIsTrue_WhenCurrentUserExists() = runTest {
        initViewModel()

        authRepository.currentUser.value = mockNormalUserDetail
        authRepository.emit(isReady = true)

        assertTrue(viewModel.uiState.value.hideSplashScreen)
    }

    private fun initViewModel() {
        viewModel = HomeViewModel(
            IsAuthReadyUseCase(authRepository),
            IsCurrentUserAdminUseCase(GetCurrentUserTypeUseCase(authRepository)),
            GetCurrentUserUseCase(authRepository)
        )
    }
}