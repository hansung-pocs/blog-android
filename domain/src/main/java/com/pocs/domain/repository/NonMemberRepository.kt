package com.pocs.domain.repository

import com.pocs.domain.model.nonmember.NonMemberCreateInfo

interface NonMemberRepository {

    suspend fun createNonMember(nonMemberCreateInfo: NonMemberCreateInfo): Result<Unit>
}