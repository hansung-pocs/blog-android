package com.pocs.presentation

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.GetCurrentUserUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.domain.usecase.post.AddPostUseCase
import com.pocs.presentation.view.post.create.PostCreateViewModel
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakePostRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PostCreateViewModelTest {

    private val postRepository = FakePostRepositoryImpl()
    private val authRepository = FakeAuthRepositoryImpl()

    private val viewModel = PostCreateViewModel(
        addPostUseCase = AddPostUseCase(postRepository, GetCurrentUserUseCase(authRepository)),
        isCurrentUserAdminUseCase = IsCurrentUserAdminUseCase(
            GetCurrentUserTypeUseCase(authRepository)
        )
    )

    @Test
    fun shouldIsUserAdminIsTrue_WhenCurrentUserIsAdmin() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ADMIN)

        viewModel.initUiState(PostCategory.NOTICE)

        assertTrue(viewModel.uiState.value.isUserAdmin)
    }

    @Test
    fun shouldIsUserAdminIsFalse_WhenCurrentUserIsNotAdmin() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.MEMBER)

        viewModel.initUiState(PostCategory.KNOWHOW)

        assertFalse(viewModel.uiState.value.isUserAdmin)
    }
}