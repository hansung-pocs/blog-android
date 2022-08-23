package com.pocs.domain

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAdminUseCase
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsCurrentUserAdminUseCaseTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private val isCurrentUserAdminUseCase = IsCurrentUserAdminUseCase(
        GetCurrentUserTypeUseCase(authRepository)
    )

    @Test
    fun returnsTrue_WhenCurrentUserIsAdmin() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ADMIN)

        val result = isCurrentUserAdminUseCase()
        assertTrue(result)
    }

    @Test
    fun returnsFalse_WhenCurrentUserIsNotAdmin() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.MEMBER)

        val result = isCurrentUserAdminUseCase()
        assertFalse(result)
    }
}