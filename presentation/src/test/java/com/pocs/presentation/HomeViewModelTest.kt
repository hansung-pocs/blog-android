package com.pocs.presentation

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.presentation.view.home.HomeViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private lateinit var viewModel: HomeViewModel

    @Test
    fun shouldIsCurrentUserAdminIsTrue_WhenCurrentUserIsAdmin() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.ADMIN)
        initViewModel()

        assertTrue(viewModel.isCurrentUserAdmin)
    }

    @Test
    fun shouldIsCurrentUserAdminIsFalse_WhenCurrentUserIsNotAdmin() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.MEMBER)
        initViewModel()

        assertFalse(viewModel.isCurrentUserAdmin)
    }

    private fun initViewModel() {
        viewModel = HomeViewModel(
            GetCurrentUserUseCase(authRepository)
        )
    }
}