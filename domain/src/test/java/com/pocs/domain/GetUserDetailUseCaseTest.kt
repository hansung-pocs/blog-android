package com.pocs.domain

import com.pocs.domain.model.user.UserType
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import com.pocs.domain.usecase.user.GetUserDetailUseCase
import com.pocs.test_library.fake.FakeAdminRepositoryImpl
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockKickedUserDetail
import com.pocs.test_library.mock.mockNormalUserDetail
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class GetUserDetailUseCaseTest {

    private val userRepository = FakeUserRepositoryImpl()
    private val adminRepository = FakeAdminRepositoryImpl()
    private val authRepository = FakeAuthRepositoryImpl()

    private val getUserDetailUseCase = GetUserDetailUseCase(
        userRepository = userRepository,
        adminRepository = adminRepository,
        getCurrentUserTypeUseCase = GetCurrentUserTypeUseCase(authRepository)
    )

    private val userDetailFromMember = mockNormalUserDetail
    private val userDetailFromAdmin = mockKickedUserDetail

    @Before
    fun setUp() {
        userRepository.userDetailResult = Result.success(userDetailFromMember)
        adminRepository.userDetailResult = Result.success(userDetailFromAdmin)
    }

    @Test
    fun returnsUserDetailFromAdmin_WhenCurrentUserTypeIsAdmin() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.ADMIN)

        runBlocking {
            val result = getUserDetailUseCase(userDetailFromAdmin.id)
            assertEquals(userDetailFromAdmin, result.getOrNull()!!)
        }
    }

    @Test
    fun returnsUserDetailFromMember_WhenCurrentUserTypeIsMember() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.MEMBER)

        runBlocking {
            val result = getUserDetailUseCase(userDetailFromMember.id)
            assertEquals(userDetailFromMember, result.getOrNull()!!)
        }
    }

    @Test
    fun returnsFailureResult_WhenCurrentUserTypeIsUnknown() {
        authRepository.currentUser.value = mockNormalUserDetail.copy(type = UserType.ANONYMOUS)

        runBlocking {
            val result = getUserDetailUseCase(userDetailFromMember.id)
            assertTrue(result.exceptionOrNull()!! is Exception)
        }
    }
}