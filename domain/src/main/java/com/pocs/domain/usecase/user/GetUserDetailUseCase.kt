package com.pocs.domain.usecase.user

import com.pocs.domain.model.user.UserDetail
import com.pocs.domain.model.user.UserType
import com.pocs.domain.repository.AdminRepository
import com.pocs.domain.repository.UserRepository
import com.pocs.domain.usecase.auth.GetCurrentUserTypeUseCase
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val adminRepository: AdminRepository,
    private val getCurrentUserTypeUseCase: GetCurrentUserTypeUseCase
) {
    /**
     * [id]가 아이디인 회원의 [UserDetail] 결과를 반환한다.
     *
     * 현재 로그인한 유저의 권한에 따라 다른 값이 반환된다.
     * - [UserType.ADMIN]: 탈퇴 여부까지 포함된 유저의 자세한 결과
     * - [UserType.MEMBER]: 탈퇴 여부를 제외한 유저의 자세한 결과
     * - [UserType.UNKNOWN]: 열람할 권한이 없어 [Result.failure]를 반환한다.
     */
    suspend operator fun invoke(id: Int): Result<UserDetail> {
        return when(getCurrentUserTypeUseCase()) {
            UserType.ADMIN -> adminRepository.getUserDetail(id)
            UserType.MEMBER -> userRepository.getUserDetail(id)
            UserType.UNKNOWN -> Result.failure(Exception("회원만 열람할 수 있습니다."))
        }
    }
}