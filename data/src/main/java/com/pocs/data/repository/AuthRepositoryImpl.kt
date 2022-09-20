package com.pocs.data.repository

import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toDetailEntity
import com.pocs.data.model.auth.AuthLocalData
import com.pocs.data.model.auth.LoginRequestBody
import com.pocs.data.source.AuthLocalDataSource
import com.pocs.data.source.AuthRemoteDataSource
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.repository.AuthRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {

    private val isReady = MutableStateFlow(false)

    private val currentUserState: MutableStateFlow<UserDetail?> = MutableStateFlow(null)

    init {
        MainScope().launch {
            if (localDataSource.hasData()) {
                try {
                    val response = remoteDataSource.isSessionValid()
                    val isSessionValid = response.isSuccessful

                    if (isSessionValid) {
                        val userDto = response.body()!!.data.user
                        currentUserState.value = userDto.toDetailEntity()
                    } else {
                        localDataSource.clear()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // 인터넷 연결이 끊김 등의 예외는 무시한다.
                }
            }
            isReady.emit(true)
        }
    }

    override fun isReady(): Flow<Boolean> = isReady

    override suspend fun login(userName: String, password: String): Result<Unit> {
        try {
            val response = remoteDataSource.login(
                LoginRequestBody(userName = userName, password = password)
            )

            if (response.isSuccessful) {
                val loginResponseData = response.body()!!.data

                currentUserState.value = loginResponseData.user.toDetailEntity()
                localDataSource.setData(
                    authLocalData = AuthLocalData(
                        sessionToken = loginResponseData.sessionToken
                    )
                )

                return Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val response = remoteDataSource.logout()

            if (response.isSuccessful) {
                currentUserState.value = null
                localDataSource.clear()

                Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): StateFlow<UserDetail?> {
        return currentUserState
    }

    override fun syncCurrentUser(
        name: String,
        email: String,
        company: String?,
        github: String?
    ) {
        val currentUserStateValue = currentUserState.value
        if (currentUserStateValue != null) {
            currentUserState.value = currentUserStateValue.copy(
                defaultInfo = currentUserStateValue.defaultInfo?.copy(
                    name = name,
                    email = email,
                    company = company,
                    github = github
                )
            )
        }
    }

    override fun syncCurrentUserProfileImage(url: String?) {
        val currentUserStateValue = currentUserState.value
        if (currentUserStateValue != null) {
            currentUserState.value = currentUserStateValue.copy(
                defaultInfo = currentUserStateValue.defaultInfo?.copy(
                    profileImageUrl = url
                )
            )
        }
    }
}
