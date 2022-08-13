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

    private var token: String? = null

    init {
        val localData = localDataSource.getData()
        MainScope().launch {
            if (localData != null) {
                try {
                    val response = remoteDataSource.isSessionValid(localData.sessionToken)
                    val isSessionValid = response.isSuccessful

                    if (isSessionValid) {
                        val userDto = response.body()!!.data
                        currentUserState.value = userDto.toDetailEntity()
                        token = localData.sessionToken
                    } else {
                        localDataSource.clear()
                    }
                } catch (e: Exception) {
                    // 인터넷 연결이 끊김 등의 예외는 무시한다.
                }
            }
            isReady.emit(true)
        }
    }

    override fun isReady(): Flow<Boolean> = isReady

    override suspend fun login(userName: String, password: String): Result<Unit> {
        assert(token == null) { "이미 로그인한 상태에서 로그인을 시도했습니다." }
        try {
            val response = remoteDataSource.login(
                LoginRequestBody(username = userName, password = password)
            )

            if (response.isSuccessful) {
                val loginResponseData = response.body()!!.data

                currentUserState.value = loginResponseData.user.toDetailEntity()
                token = loginResponseData.sessionToken
                localDataSource.setData(AuthLocalData(sessionToken = token!!))

                return Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        assert(token != null) { "로그인하지 않은 상태로 로그아웃을 시도했습니다." }
        return try {
            val response = remoteDataSource.logout(token!!)

            if (response.isSuccessful) {
                currentUserState.value = null
                token = null
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
        currentUserState.value = currentUserState.value?.copy(
            name = name,
            email = email,
            company = company,
            github = github
        )
    }
}