package com.pocs.data

import com.pocs.data.model.auth.AuthLocalData
import com.pocs.data.model.auth.LoginResponseData
import com.pocs.data.repository.AuthRepositoryImpl
import com.pocs.test_library.fake.source.remote.FakeAuthRemoteDataSource
import com.pocs.test_library.fake.FakeUserRepositoryImpl
import com.pocs.test_library.fake.source.local.FakeAuthLocalDataSource
import com.pocs.test_library.mock.errorResponse
import com.pocs.test_library.mock.mockNormalUserDetail
import com.pocs.test_library.mock.successResponse
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class AuthRepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val userRepository = FakeUserRepositoryImpl()
    private val remoteDataSource = FakeAuthRemoteDataSource()
    private val localDataSource = FakeAuthLocalDataSource()

    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun currentUserIsNull_WhenRunningAppFirstTime() = runTest {
        localDataSource.authLocalData = null

        initRepository()

        assertNull(repository.getCurrentUser().value)
    }

    @Test
    fun currentUserExists_WhenUserAlreadyLoggedInBefore() {
        val userDetail = mockNormalUserDetail
        localDataSource.authLocalData = AuthLocalData("abc", userDetail.id)
        remoteDataSource.isSessionValidResponse = successResponse(Unit)
        userRepository.userDetailResult = Result.success(userDetail)

        initRepository()

        assertEquals(userDetail, repository.getCurrentUser().value)
    }

    @Test
    fun clearLocalSession_WhenRemoteSessionIsExpired() {
        val userDetail = mockNormalUserDetail
        localDataSource.authLocalData = AuthLocalData("abc", userDetail.id)
        remoteDataSource.isSessionValidResponse = errorResponse()
        userRepository.userDetailResult = Result.success(userDetail)

        initRepository()

        assertNull(repository.getCurrentUser().value)
        assertNull(localDataSource.authLocalData)
    }

    @Test
    fun currentUserExists_WhenSuccessToLogin() = runTest {
        val userDetail = mockNormalUserDetail
        localDataSource.authLocalData = null
        remoteDataSource.loginResponse = successResponse(
            LoginResponseData(sessionToken = "abc", userId = userDetail.id)
        )
        userRepository.userDetailResult = Result.success(userDetail)
        initRepository()

        assertNull(repository.getCurrentUser().value)

        repository.login("id", "password")

        assertEquals(userDetail, repository.getCurrentUser().value)
    }

    @Test
    fun saveAuthLocalData_WhenSuccessToLogin() = runTest {
        val token = "abc"
        val userDetail = mockNormalUserDetail
        localDataSource.authLocalData = null
        userRepository.userDetailResult = Result.success(userDetail)
        remoteDataSource.loginResponse = successResponse(
            LoginResponseData(sessionToken = token, userId = userDetail.id)
        )
        initRepository()

        assertNull(localDataSource.authLocalData)

        repository.login("id", "password")

        assertEquals(token, localDataSource.authLocalData!!.token)
        assertEquals(userDetail.id, localDataSource.authLocalData!!.userId)
    }

    @Test
    fun returnsFailureResult_WhenFailedToLogin() = runTest {
        localDataSource.authLocalData = null
        remoteDataSource.loginResponse = errorResponse()
        initRepository()

        val result = repository.login("id", "password")

        assertTrue(result.isFailure)
    }

    @Test
    fun currentUserIsNull_WhenLoggedOut() = runTest {
        val userDetail = mockNormalUserDetail
        localDataSource.authLocalData = AuthLocalData("abc", 1)
        userRepository.userDetailResult = Result.success(userDetail)
        remoteDataSource.logoutResponse = successResponse(Unit)
        initRepository()

        assertEquals(repository.getCurrentUser().value, userDetail)

        repository.logout()

        assertNull(repository.getCurrentUser().value)
    }

    @Test
    fun clearLocalAuthData_WhenLoggedOut() = runTest {
        val userDetail = mockNormalUserDetail
        localDataSource.authLocalData = AuthLocalData("abc", 1)
        userRepository.userDetailResult = Result.success(userDetail)
        remoteDataSource.logoutResponse = successResponse(Unit)
        initRepository()

        repository.logout()

        assertNull(localDataSource.authLocalData)
    }

    @Test
    fun emitTrueFromIsReady_WhenLocalDataIsValid() = runTest {
        val userDetail = mockNormalUserDetail
        localDataSource.authLocalData = AuthLocalData("abc", 1)
        userRepository.userDetailResult = Result.success(userDetail)

        initRepository()
        var isReady = false
        val job = launch(testDispatcher) {
            repository.isReady().collectLatest {
                isReady = it
            }
        }

        assertTrue(isReady)

        job.cancel()
    }

    @Test
    fun emitTrueFromIsReady_WhenThereIsNoLocalData() = runTest {
        localDataSource.authLocalData = null

        initRepository()
        var isReady = false
        val job = launch(testDispatcher) {
            repository.isReady().collectLatest {
                isReady = it
            }
        }

        assertTrue(isReady)

        job.cancel()
    }

    private fun initRepository() {
        repository = AuthRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            userRepository = userRepository
        )
    }
}
