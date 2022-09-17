package com.pocs.presentation

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.presentation.view.home.HomeViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private lateinit var viewModel: HomeViewModel

    @Test
    fun shouldIsCurrentUserAdminIsTrue_WhenCurrentUserIsAdmin() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ADMIN)
        initViewModel()

        assertTrue(viewModel.uiState.value.isCurrentUserAdmin)
    }

    @Test
    fun shouldIsCurrentUserAdminIsFalse_WhenCurrentUserIsNotAdmin() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.MEMBER)
        initViewModel()

        assertFalse(viewModel.uiState.value.isCurrentUserAdmin)
    }

    private fun initViewModel() {
        viewModel = HomeViewModel(GetCurrentUserTypeUseCase(authRepository))
    }
}
