package com.pocs.presentation

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.domain.usecase.post.UpdatePostUseCase
import com.pocs.presentation.view.post.edit.PostEditViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PostEditViewModelTest {

    private val postRepository = FakePostRepositoryImpl()
    private val authRepository = FakeAuthRepositoryImpl()

    private val viewModel = PostEditViewModel(
        updatePostUseCase = UpdatePostUseCase(postRepository, authRepository),
        isCurrentUserAdminUseCase = IsCurrentUserAdminUseCase(
            GetCurrentUserTypeUseCase(authRepository)
        )
    )

    @Test
    fun shouldIsUserAdminIsTrue_WhenCurrentUserIsAdmin() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.ADMIN)

        viewModel.initUiState(1, "", "", PostCategory.NOTICE)

        assertTrue(viewModel.uiState.value.isUserAdmin)
    }

    @Test
    fun shouldIsUserAdminIsFalse_WhenCurrentUserIsNotAdmin() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.MEMBER)

        viewModel.initUiState(1, "", "", PostCategory.KNOWHOW)

        assertFalse(viewModel.uiState.value.isUserAdmin)
    }
}