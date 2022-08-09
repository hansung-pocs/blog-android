package com.pocs.domain

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.auth.IsCurrentUserUnknownUseCase
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsCurrentUserUnknownUseCaseTest {

    private val authRepository = FakeAuthRepositoryImpl()

    private val isCurrentUserUnknownUseCase = IsCurrentUserUnknownUseCase(
        GetCurrentUserTypeUseCase(authRepository)
    )

    @Test
    fun returnsTrue_WhenCurrentUserIsUnknown() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.UNKNOWN)

        val result = isCurrentUserUnknownUseCase()
        assertTrue(result)
    }

    @Test
    fun returnsFalse_WhenCurrentUserIsNotUnknown() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.MEMBER)

        val result = isCurrentUserUnknownUseCase()
        assertFalse(result)
    }
}