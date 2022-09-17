package com.pocs.domain

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAnonymousUseCase
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsCurrentUserAnonymousUseCaseTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private val isCurrentUserAnonymousUseCase = IsCurrentUserAnonymousUseCase(
        GetCurrentUserTypeUseCase(authRepository)
    )

    @Test
    fun returnsTrue_WhenCurrentUserIsAnonymous() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.ANONYMOUS)

        val result = isCurrentUserAnonymousUseCase()
        assertTrue(result)
    }

    @Test
    fun returnsFalse_WhenCurrentUserIsNotAnonymous() {
        authRepository.currentUser.value = mockAdminUserDetail.copy(type = UserType.MEMBER)

        val result = isCurrentUserAnonymousUseCase()
        assertFalse(result)
    }
}
