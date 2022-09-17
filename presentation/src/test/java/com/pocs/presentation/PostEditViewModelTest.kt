package com.pocs.presentation

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.post.UpdatePostUseCase
import com.pocs.presentation.view.post.edit.PostEditViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PostEditViewModelTest {

    private val postRepository = FakePostRepositoryImpl()
    private val authRepository = FakeAuthRepositoryImpl()

    private val viewModel = PostEditViewModel(
        updatePostUseCase = UpdatePostUseCase(postRepository, authRepository),
        getCurrentUserTypeUseCase = GetCurrentUserTypeUseCase(authRepository)
    )

    @Test
    fun shouldIsUserAdminIsTrue_WhenCurrentUserIsAdmin() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ADMIN)

        viewModel.initUiState(1, "", "", PostCategory.NOTICE, true)

        assertTrue(viewModel.uiState.value.isUserAdmin)
    }

    @Test
    fun shouldIsUserAdminIsFalse_WhenCurrentUserIsNotAdmin() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.MEMBER)

        viewModel.initUiState(1, "", "", PostCategory.KNOWHOW, true)

        assertFalse(viewModel.uiState.value.isUserAdmin)
    }

    @Test
    fun shouldShowOnlyMemberButtonIsFalse_WhenCurrentUserIsAnonymous() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ANONYMOUS)

        viewModel.initUiState(1, "", "", PostCategory.QNA, false)

        assertFalse(viewModel.uiState.value.showOnlyMemberButton)
    }

    @Test
    fun shouldShowOnlyMemberButtonIsTrue_WhenCurrentUserIsMember() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.MEMBER)

        viewModel.initUiState(1, "", "", PostCategory.QNA, false)

        assertTrue(viewModel.uiState.value.showOnlyMemberButton)
    }
}
