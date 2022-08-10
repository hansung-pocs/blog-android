package com.pocs.data.repository

import com.pocs.data.extension.errorMessage
import com.pocs.data.model.auth.AuthLocalData
import com.pocs.data.model.auth.LoginRequestBody
import com.pocs.data.source.AuthLocalDataSource
import com.pocs.data.source.AuthRemoteDataSource
import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.repository.AuthRepository
import com.pocs.domain.repository.UserRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource,
    private val userRepository: UserRepository
) : AuthRepository {

    private val currentUserState: MutableStateFlow<UserDetail?> = MutableStateFlow(null)

    private var token: String? = null

    init {
        initAuth()
    }

    private fun initAuth() {
        val localData = localDataSource.getData()
        if (localData != null) {
            MainScope().launch {
                val response = remoteDataSource.isSessionValid(localData.token)
                val isSessionValid = response.isSuccessful

                if (isSessionValid) {
                    val userDetailResult = userRepository.getUserDetail(localData.userId)

                    if (userDetailResult.isSuccess) {
                        currentUserState.value = userDetailResult.getOrNull()!!
                        token = localData.token
                    }
                } else {
                    localDataSource.clear()
                }
            }
        }
    }

    override suspend fun login(userName: String, password: String): Result<Unit> {
        if (token != null) {
            throw IllegalStateException("이미 로그인한 상태에서 로그인을 시도했습니다.")
        }
        try {
            val response = remoteDataSource.login(
                LoginRequestBody(username = userName, password = password)
            )

            if (response.isSuccessful) {
                val loginResponseData = response.body()!!.data
                val userDetailResult = userRepository.getUserDetail(loginResponseData.userId)

                if (userDetailResult.isSuccess) {
                    val userDetail = userDetailResult.getOrNull()!!

                    currentUserState.value = userDetail
                    token = loginResponseData.sessionToken
                    localDataSource.setData(AuthLocalData(token = token!!, userId = userDetail.id))

                    return Result.success(Unit)
                }
                throw Exception("로그인에 성공하였으나 유저 정보를 얻지 못함: ${userDetailResult.exceptionOrNull()!!.message}")
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        if (token == null) {
            throw IllegalStateException("로그인하지 않은 상태로 로그아웃을 시도했습니다.")
        }
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
}