package com.pocs.domain.repository

import com.pocs.domain.model.user.UserDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    /**
     * 초기에는 `false`이며, 로컬 세션 토큰 유효성 검사와 유저 정보를 얻는 과정이 끝나면 `true`가 방출된다.
     *
     * 로컬 정보가 없다면 곧바로 `true`가 방출된다. 로컬 정보가 존재하여 로컬 세션 토큰 유효성 검사에 통과하면
     * 유저 정보를 얻는다. 그리고 [getCurrentUser]를 통해 유저 정보가 방출되고나서 [isReady]에 `true`가 방출된다.
     */
    fun isReady(): Flow<Boolean>

    suspend fun login(userName: String, password: String): Result<Unit>

    suspend fun logout(): Result<Unit>

    /**
     * 현재 로그인한 유저의 흐름을 얻는다.
     *
     * 로그인하지 않은 경우 흐름 값은 `null`이다. 이미 로그인한 상태에서 앱 실행시 세션 토큰과 유저 정보를 얻고 나면 곧바로
     * 이곳에 얻은 유저 정보를 방출한다. 이는 [isReady]에 `true`가 방출되기 전에 수행된다.
     */
    fun getCurrentUser(): StateFlow<UserDetail?>

    /**
     * 메모리에 존재하는 현재 유저의 정보를 동기화한다.
     *
     * 이는 유저가 개인 정보를 수정하고 나서 서버의 데이터와 현재 앱 실행중의 메모리 데이터를 동기화 하기 위해 사용한다.
     */
    fun syncCurrentUser(
        name: String,
        email: String,
        company: String?,
        github: String?
    )

    fun syncCurrentUserProfileImage(url: String?)
}