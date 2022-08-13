package com.pocs.data

import com.pocs.data.mapper.toDetailEntity
import com.pocs.data.model.auth.AuthLocalData
import com.pocs.data.model.auth.LoginResponseData
import com.pocs.data.repository.AuthRepositoryImpl
import com.pocs.test_library.fake.source.remote.FakeAuthRemoteDataSource
import com.pocs.test_library.fake.source.local.FakeAuthLocalDataSource
import com.pocs.test_library.mock.errorResponse
import com.pocs.test_library.mock.mockUserDto
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
import java.net.ConnectException

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class AuthRepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()

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
        val userDto = mockUserDto
        localDataSource.authLocalData = AuthLocalData("abc")
        remoteDataSource.isSessionValidResponse = successResponse(userDto)

        initRepository()

        assertEquals(userDto.toDetailEntity(), repository.getCurrentUser().value)
    }

    @Test
    fun clearLocalSession_WhenRemoteSessionIsExpired() {
        localDataSource.authLocalData = AuthLocalData("abc")
        remoteDataSource.isSessionValidResponse = errorResponse()

        initRepository()

        assertNull(repository.getCurrentUser().value)
        assertNull(localDataSource.authLocalData)
    }

    @Test
    fun currentUserExists_WhenSuccessToLogin() = runTest {
        val userDto = mockUserDto
        localDataSource.authLocalData = null
        remoteDataSource.loginResponse = successResponse(
            LoginResponseData(sessionToken = "abc", user = userDto)
        )
        initRepository()

        assertNull(repository.getCurrentUser().value)

        repository.login("id", "password")

        assertEquals(userDto.toDetailEntity(), repository.getCurrentUser().value)
    }

    @Test
    fun saveAuthLocalData_WhenSuccessToLogin() = runTest {
        val token = "abc"
        val userDto = mockUserDto
        localDataSource.authLocalData = null
        remoteDataSource.loginResponse = successResponse(
            LoginResponseData(sessionToken = token, user = userDto)
        )
        initRepository()

        assertNull(localDataSource.authLocalData)

        repository.login("id", "password")

        assertEquals(token, localDataSource.authLocalData!!.sessionToken)
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
        localDataSource.authLocalData = AuthLocalData("abc")
        remoteDataSource.isSessionValidResponse = successResponse(mockUserDto)
        remoteDataSource.logoutResponse = successResponse(Unit)
        initRepository()

        assertNotNull(repository.getCurrentUser().value)

        repository.logout()

        assertNull(repository.getCurrentUser().value)
    }

    @Test
    fun clearLocalAuthData_WhenLoggedOut() = runTest {
        localDataSource.authLocalData = AuthLocalData("abc")
        remoteDataSource.isSessionValidResponse = successResponse(mockUserDto)
        remoteDataSource.logoutResponse = successResponse(Unit)
        initRepository()

        repository.logout()

        assertNull(localDataSource.authLocalData)
    }

    @Test
    fun emitTrueFromIsReady_WhenLocalDataIsValid() = runTest {
        localDataSource.authLocalData = AuthLocalData("abc")

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

    @Test
    fun emitTrueFromIsReady_WhenInternetIsNotConnected() = runTest {
        remoteDataSource.isSessionValidInnerLambda = { throw ConnectException() }
        localDataSource.authLocalData = AuthLocalData("abc")

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
            localDataSource = localDataSource
        )
    }
}
