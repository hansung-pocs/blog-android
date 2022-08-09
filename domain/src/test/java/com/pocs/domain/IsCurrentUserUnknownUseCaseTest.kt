package com.pocs.domain

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.IsCurrentUserUnknownUseCase
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsCurrentUserUnknownUseCaseTest {

    private val userRepository = FakeUserRepositoryImpl()

    private val isCurrentUserUnknownUseCase = IsCurrentUserUnknownUseCase(
        GetCurrentUserTypeUseCase(userRepository)
    )

    @Test
    fun returnsTrue_WhenCurrentUserIsUnknown(){
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.UNKNOWN)

        val result = isCurrentUserUnknownUseCase()
        assertTrue(result)
    }

    @Test
    fun returnsFalse_WhenCurrentUserIsNotUnknown(){
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.MEMBER)

        val result = isCurrentUserUnknownUseCase()
        assertFalse(result)
    }
}