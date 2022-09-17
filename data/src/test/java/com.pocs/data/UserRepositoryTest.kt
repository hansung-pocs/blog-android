package com.pocs.data

import com.pocs.data.api.UserApi
import com.pocs.data.repository.UserRepositoryImpl
import com.pocs.test_library.fake.FakeAuthRepositoryImpl
import com.pocs.test_library.fake.source.remote.FakeUserRemoteDataSource
import com.pocs.test_library.mock.mockMemberUserDetail
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Retrofit
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class UserRepositoryTest {

    private val dataSource = FakeUserRemoteDataSource()
    private val authRepository = FakeAuthRepositoryImpl()

    private val repository = UserRepositoryImpl(
        api = Retrofit.Builder()
            .baseUrl("https://www.google.com")
            .build()
            .create(UserApi::class.java),
        dataSource = dataSource,
        authRepository = authRepository
    )

    @Test
    fun shouldCallUploadImage_WhenProfileImageIsNotNull() = runTest {
        var count = 0
        dataSource.uploadProfileImageCallBack = {
            count++
        }

        repository.updateUser(
            id = 1,
            password = null,
            name = "name",
            email = "dsifjovie@gmail.com",
            company = null,
            github = null,
            useDefaultProfileImage = false,
            newProfileImage = File("mock")
        )

        assertEquals(1, count)
    }

    @Test
    fun shouldCallUploadImage_WhenUsingDefaultImage() = runTest {
        var count = 0
        dataSource.uploadProfileImageCallBack = {
            count++
        }

        repository.updateUser(
            id = 1,
            password = null,
            name = "name",
            email = "dsifjovie@gmail.com",
            company = null,
            github = null,
            useDefaultProfileImage = true,
            newProfileImage = null
        )

        assertEquals(1, count)
    }

    @Test
    fun shouldNotCallUploadImage_WhenImageIsNullAndUseDefaultProfileImageIsFalse() = runTest {
        var count = 0
        dataSource.uploadProfileImageCallBack = {
            count++
        }

        repository.updateUser(
            id = 1,
            password = null,
            name = "name",
            email = "dsifjovie@gmail.com",
            company = null,
            github = null,
            useDefaultProfileImage = false,
            newProfileImage = null
        )

        assertEquals(0, count)
    }

    @Test
    fun shouldSyncUserProfileImage_WhenUpdateUserProfileAsDefaultImage() = runTest {
        authRepository.currentUser.value = mockMemberUserDetail.copy(
            defaultInfo = mockMemberUserDetail.defaultInfo?.copy(profileImageUrl = "url")
        )

        repository.updateUser(
            id = 1,
            password = null,
            name = "name",
            email = "dsifjovie@gmail.com",
            company = null,
            github = null,
            useDefaultProfileImage = true,
            newProfileImage = null
        )

        assertNull(authRepository.currentUser.value?.defaultInfo?.profileImageUrl)
    }
}
