package com.pocs.domain

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserAnonymousUseCase
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsCurrentUserAnonymousUseCaseTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private val isCurrentUserAnonymousUseCase = IsCurrentUserAnonymousUseCase(
        GetCurrentUserTypeUseCase(authRepository)
    )

    @Test
    fun returnsTrue_WhenCurrentUserIsUnknown() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.ANONYMOUS)

        val result = isCurrentUserAnonymousUseCase()
        assertTrue(result)
    }

    @Test
    fun returnsFalse_WhenCurrentUserIsNotUnknown() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.MEMBER)

        val result = isCurrentUserAnonymousUseCase()
        assertFalse(result)
    }
}