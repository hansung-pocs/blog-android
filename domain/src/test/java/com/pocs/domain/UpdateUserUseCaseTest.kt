package com.pocs.domain

import com.pocs.domain.usecase.user.UpdateUserUseCase
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.mock.mockAdminUserDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateUserUseCaseTest {

    private val authRepository = FakeAuthRepositoryImpl()
    private val userRepository = FakeUserRepositoryImpl()

    private val updateUserUseCase = UpdateUserUseCase(
        authRepository = authRepository,
        userRepository = userRepository
    )

    @Test
    fun shouldSyncAuthCurrentUser_WhenSuccessToUpdateUser() = runTest {
        val updatedName = "wow"
        val userDetail = mockAdminUserDetail
        authRepository.currentUser.value = mockAdminUserDetail
        userRepository.updateUserResult = Result.success(Unit)

        updateUserUseCase(
            id = userDetail.id,
            password = "",
            name = updatedName,
            email = userDetail.defaultInfo!!.email,
            company = userDetail.defaultInfo!!.company,
            github = userDetail.defaultInfo!!.github,
            useDefaultProfileImage = false,
            newProfileImage = null
        )

        assertEquals(updatedName, authRepository.currentUser.value?.defaultInfo!!.name)
    }
}
