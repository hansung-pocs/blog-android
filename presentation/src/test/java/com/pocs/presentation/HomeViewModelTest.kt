package com.pocs.presentation

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.user.GetCurrentUserDetailUseCase
import com.pocs.presentation.view.home.HomeViewModel
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {

    private val userRepository = FakeUserRepositoryImpl()

    private lateinit var viewModel: HomeViewModel

    @Test
    fun shouldIsCurrentUserAdminIsTrue_WhenCurrentUserIsAdmin() {
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.ADMIN)
        initViewModel()

        assertTrue(viewModel.isCurrentUserAdmin)
    }

    @Test
    fun shouldIsCurrentUserAdminIsFalse_WhenCurrentUserIsNotAdmin() {
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.MEMBER)
        initViewModel()

        assertFalse(viewModel.isCurrentUserAdmin)
    }

    private fun initViewModel() {
        viewModel = HomeViewModel(
            GetCurrentUserDetailUseCase(userRepository)
        )
    }
}