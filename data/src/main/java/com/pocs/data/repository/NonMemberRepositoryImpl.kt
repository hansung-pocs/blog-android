package com.pocs.data.repository

import com.pocs.data.extension.errorMessage
import com.pocs.data.mapper.toDto
import com.pocs.data.source.NonMemberRemoteDataSource
import com.pocs.domain.model.nonmember.NonMemberCreateInfo
import com.pocs.domain.repository.NonMemberRepository

class NonMemberRepositoryImpl(
    private val dataSource: NonMemberRemoteDataSource
) : NonMemberRepository {

    override suspend fun createNonMember(nonMemberCreateInfo: NonMemberCreateInfo): Result<Unit> {
        return try {
            val response = dataSource.createNonMember(nonMemberCreateInfo.toDto())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw Exception(response.errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}