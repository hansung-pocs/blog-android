package com.pocs.domain.usecase.nonmember

import com.pocs.domain.model.nonmember.NonMemberCreateInfo
import com.pocs.domain.repository.NonMemberRepository
import javax.inject.Inject

class CreateNonMemberUseCase @Inject constructor(
    private val repository: NonMemberRepository
) {
    suspend operator fun invoke(
        nonMemberCreateInfo: NonMemberCreateInfo
    ) = repository.createNonMember(nonMemberCreateInfo = nonMemberCreateInfo)
}