package com.pocs.domain

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.user.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.IsCurrentUserAdminUseCase
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockNormalUserDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsCurrentUserAdminUseCaseTest {

    private val userRepository = FakeUserRepositoryImpl()

    private val isCurrentUserAdminUseCase = IsCurrentUserAdminUseCase(
        GetCurrentUserTypeUseCase(userRepository)
    )

    @Test
    fun returnsTrue_WhenCurrentUserIsAdmin(){
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.ADMIN)

        val result = isCurrentUserAdminUseCase()
        assertTrue(result)
    }

    @Test
    fun returnsFalse_WhenCurrentUserIsNotAdmin(){
        userRepository.currentUser = mockNormalUserDetail.copy(type = UserType.MEMBER)

        val result = isCurrentUserAdminUseCase()
        assertFalse(result)
    }
}